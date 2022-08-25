package com.kob.backend.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

  //static对所有实例都可见，相当于所有实例的全局变量
  //线程安全的map
  final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

  private User user;
  //链接用session维护
  private Session session = null;

  private static UserMapper userMapper;
  public static RecordMapper recordMapper;
  private static RestTemplate restTemplate;
  private Game game = null;
  private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
  private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";


  @Autowired
  public void setUserMapper(UserMapper userMapper) {
    //静态变量访问时用类名访问，用this.userMapper不行的
    WebSocketServer.userMapper = userMapper;
  }

  @Autowired
  public void setRecordMapper(RecordMapper recordMapper) {
    WebSocketServer.recordMapper = recordMapper;
  }

  @Autowired
  public void setRestTemplate(RestTemplate restTemplate) {
    WebSocketServer.restTemplate =restTemplate;
  }


  @OnOpen
  public void onOpen(Session session, @PathParam("token") String token) throws IOException {
    // 建立连接
    this.session = session;
    System.out.println("connected");
    Integer userId = JwtAuthentication.getUserId(token);
    this.user = userMapper.selectById(userId);

    if (this.user != null) {
      users.put(userId, this);
    } else {
      this.session.close();
    }
    System.out.println(users);
  }

  @OnClose
  public void onClose() {
    // 关闭链接
    System.out.println("disconnected");
    if (this.user != null) {
      users.remove(this.user.getId());
    }
  }

  public static void startGame(Integer aId, Integer bId) {
    User a = userMapper.selectById(aId), b = userMapper.selectById(bId);

    Game game = new Game(13,14,20,a.getId(),b.getId());
    game.createMap();

    if(users.get(a.getId()) != null)
      users.get(a.getId()).game = game;
    if(users.get(b.getId()) != null)
      users.get(b.getId()).game = game;
    game.start();

    JSONObject respGame= new JSONObject();
    respGame.put("a_id",game.getPlayerA().getId());
    respGame.put("a_sx", game.getPlayerA().getSx());
    respGame.put("a_sy",game.getPlayerA().getSy());
    respGame.put("b_id",game.getPlayerB().getId());
    respGame.put("b_sx", game.getPlayerB().getSx());
    respGame.put("b_sy",game.getPlayerB().getSy());
    respGame.put("map",game.getG());

    //给a的resp
    JSONObject respA =new JSONObject();
    respA.put("event","start-matching");
    respA.put("opponent_username",b.getUsername());//给a传b
    respA.put("opponent_photo", b.getPhoto());
    respA.put("game",respGame);
    if(users.get(a.getId())!= null)
      users.get(a.getId()).sendMessage(respA.toJSONString());

    //给b的resp
    JSONObject respB = new JSONObject();
    respB.put("event","start-matching");
    respB.put("opponent_username", a.getUsername());//给b传a
    respB.put("opponent_photo",a.getPhoto());
    respB.put("game",respGame);
    if(users.get(b.getId())!=null)
      users.get(b.getId()).sendMessage(respB.toJSONString());
  }


  private void startMatching() {
    System.out.println("start matching");
    MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
    data.add("user_id",this.user.getId().toString());
    data.add("rating",this.user.getRating().toString());
    restTemplate.postForObject(addPlayerUrl,data,String.class);
  }

  private void stopMatching() {
    System.out.println("stop matching");
    MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
    data.add("user_id",this.user.getId().toString());
    restTemplate.postForObject(removePlayerUrl,data,String.class);
  }

  private void move(int direction) {
    if(game.getPlayerA().getId().equals(user.getId())){
      game.setNextStepA(direction);
    } else if(game.getPlayerB().getId().equals(user.getId())) {
      game.setNextStepB(direction);
    }
  }


  @OnMessage
  public void onMessage(String message, Session session) { //onMessage当作一个路由
    // 后端从Client接收消息

    System.out.println("receive message");
    JSONObject data = JSONObject.parseObject(message);
    String event = data.getString("event");
    if ("start-matching".equals(event)) {
      startMatching();
    } else if ("stop-matching".equals(event)) {
      stopMatching();
    } else if ("move".equals(event)) {
      move(data.getInteger("direction"));
    }
  }

  @OnError
  public void onError(Session session, Throwable error) {
    error.printStackTrace();
  }

  //后端向前端发信息
  public void sendMessage(String message) {
    synchronized (this.session) {
      try {
        this.session.getBasicRemote().sendText(message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}


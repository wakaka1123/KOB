import { createRouter, createWebHistory } from "vue-router";
import BattleIndexView from "../views/battle/BattleIndexView";
import RecordsIndexView from "../views/records/RecordsIndexView";
import RankingIndexView from "../views/ranking/RankingIndexView";
import UserBotsIndexView from "../views/user/bots/UserBotsIndexView";
import NotFound from "../views/error/NotFound";
import UserAccountLoginView from "../views/user/account/UserAccountLoginView";
import UserAccountRegisterView from "../views/user/account/UserAccountRegisterView";

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/battle/",
  },
  {
    path: "/battle/",
    name: "battle_index",
    component: BattleIndexView,
  },
  {
    path: "/records/",
    name: "records",
    component: RecordsIndexView,
  },
  {
    path: "/ranking/",
    name: "ranking",
    component: RankingIndexView,
  },
  {
    path: "/user/bots/",
    name: "user_bots_index",
    component: UserBotsIndexView,
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

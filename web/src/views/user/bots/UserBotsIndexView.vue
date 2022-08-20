<template>
  <ContentField> My Bots</ContentField>
</template>

<script>
import ContentField from "../../../components/ContentField.vue";
import $ from "jquery";
import { useStore } from "vuex";

export default {
  components: { ContentField },
  setup() {
    const store = useStore();
    // $.ajax({
    //   url: "http://localhost:3000/user/bots/add/",
    //   type: "POST",
    //   data: {
    //     title: "Bot title",
    //     description: "Bot description",
    //     content: "Bot content",
    //   },
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });
    // $.ajax({
    //   url: "http://localhost:3000/user/bots/remove/",
    //   type: "POST",
    //   data: {
    //     bot_id: 22,
    //   },
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });
    // $.ajax({
    //   url: "http://localhost:3000/user/bots/update/",
    //   type: "POST",
    //   data: {
    //     bot_id: 24,
    //     title: "Bot title23",
    //     description: "Bot description23",
    //     content: "Bot content23",
    //   },
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });
    $.ajax({
      url: "http://localhost:3000/user/bots/getlist/",
      type: "get",
      headers: {
        Authorization: "Bearer " + store.state.user.token,
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      },
    });
  },
};
</script>

<style scoped></style>

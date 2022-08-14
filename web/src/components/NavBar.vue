<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <router-link class="navbar-brand" :to="{ name: 'home' }"
        >King of Bots</router-link
      >
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'battle_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'battle_index' }"
              >Battle</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="route_name == 'records' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'records' }"
              >Records</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="route_name == 'ranking' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'ranking' }"
              >Ranking</router-link
            >
          </li>
        </ul>
        <ul class="navbar-nav">
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              User
            </a>
            <ul class="dropdown-menu">
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'user_bots_index' }"
                  >My Bots</router-link
                >
              </li>
              <li><hr class="dropdown-divider" /></li>
              <li><a class="dropdown-item" href="#">Exit</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRoute } from "vue-router";
import { computed } from "vue";

export default {
  setup() {
    const route = useRoute();
    let route_name = computed(() => route.name);
    return {
      route_name,
    };
  },
};
</script>

<style scoped></style>

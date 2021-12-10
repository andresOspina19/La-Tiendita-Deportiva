<template>
  <div id="mi-app" class="app">
    <header>
      <div class = "logo">
        <img src = "@/assets/logo.svg" v-on:click="loadHome"/>
      </div>
      <!--
      <div id="app" class="antialiased text-gray-900 flex flex-col min-h-screen bg-gray-200">
          <div class="flex justify-center items-center flex-1">
            <AppDropdown>
              <template slot="toggler">
                <button
                  class="relative flex items-center focus:outline-none pl-5 pr-3 py-2 rounded-lg bg-gray-300 text-gray-800 font-semibold"
                >
                  Click me
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    class="ml-1 h-5 w-5 fill-current text-gray-700"
                  >
                    <path
                      d="M15.3 9.3a1 1 0 0 1 1.4 1.4l-4 4a1 1 0 0 1-1.4 0l-4-4a1 1 0 0 1 1.4-1.4l3.3 3.29 3.3-3.3z"
                    ></path>
                  </svg>
                </button>
              </template>
              <AppDropdownContent>
                <AppDropdownItem>Action 1</AppDropdownItem>
                <AppDropdownItem>Action 2</AppDropdownItem>
                <AppDropdownItem>Action 3</AppDropdownItem>
              </AppDropdownContent>
            </AppDropdown>
          </div>
        </div>
        -->
      <div>
        <nav class="nav-option">
          <div class="search">
            <input type="text" v-model="buscar" class="form-control" placeholder="Buscar"/>
          </div>
          <button v-if="!is_auth" v-on:click="loadLogIn"><img src="@/assets/user.svg"/></button>
          <button v-if="is_auth" v-on:click="loadSignUp">Registrarse</button>
          <button v-if="is_auth" v-on:click="logOut">Cerrar Sesión</button>
          <button v-if="!is_auth" v-on:click="loadCart"><img src="@/assets/cart.svg"/></button>
        </nav>
      </div>
    </header>

    <main>
      <router-view
        v-on:completedLogIn="completedLogIn"
        v-on:completedSignUp="completedSignUp"
        v-on:logOut="logOut"
      >
      </router-view>
    </main>

    <footer>
      <div class = "logo">
        <img id="footer-img" src = "@/assets/logo2.svg">
      </div>
      <div class="footer-text">
        <div class="col1">
              <h3 class="text-light font-weight-bold pb-2">Información</h3>
              <a class="footer-link font-weight-light" href="#">Acerca de nosotros</a><br>
              <a class="footer-link font-weight-light" href="#">Política de privacidad</a><br>
              <a class="footer-link font-weight-light" href="#">Términos y condiciones</a><br>
        </div>

        <div class="col2">
              <h3 class="text-light font-weight-bold">Contáctanos</h3>
              <a class="footer-link font-weight-light" href="#">Calle 11 #22-33</a><br>
              <a class="footer-link font-weight-light" href="#">(+57 555-5555)</a><br>
              <a class="footer-link font-weight-light" href="#">latienditadeportiva@gmail.com</a><br>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
/*import AppDropdown from "./components/AppDropdown.vue";
import AppDropdownContent from "./components/AppDropdownContent.vue";
import AppDropdownItem from "./components/AppDropdownItem.vue";*/
export default {
  name: "App",
  /*components: {
    AppDropdown,
    AppDropdownContent,
    AppDropdownItem
  },*/
  computed: {
    is_auth: {
      get: function () {
        return this.$route.meta.requiresAuth;
      },
      set: function () {},
    },
  },
  methods: {
    loadHome: function () {
      this.$router.push({ name: "home" });
    },
    loadLogIn: function () {
      this.$router.push({ name: "logIn" });
    },
    loadSignUp: function () {
      this.$router.push({ name: "signUp" });
    },
    completedLogIn: function (data) {
      localStorage.setItem("username", data.username);
      localStorage.setItem("token_access", data.token_access);
      localStorage.setItem("token_refresh", data.token_refresh);
      alert("Autenticación Exitosa");
    },
    completedSignUp: function (data) {
      alert("Registro Exitoso");
      this.completedLogIn(data);
    },
    logOut: function () {
      localStorage.clear();
      alert("Sesión Cerrada");
      this.loadLogIn();
    },
  },
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Readex+Pro:wght@200;300;400;500;600;700&display=swap');

body{
  margin: 0;
}

.app {
  font-family: 'Readex Pro', sans-serif;
  display: grid;
  grid-template-rows: auto 1fr auto;
  width: 100%;
  height: 100vh;
  margin: 0;
  border: 0px solid #283747;
}

header,
footer {
  padding: 1rem;
}

.logo img {
  height: 14rem;
  width: 14rem;
  padding: 0,5rem 0 0 0;
}

.nav-option img {
  height: 3rem;
  width: 3rem;
}

header {
  display: flex;
  align-items: center;
  height: 8vh;
  justify-content: space-between;
  border: 0px solid #283747;
}

header .nav-option {
  display: flex;
  align-items:  center;
  padding: 10px;
  border: 0px solid #283747;
}

header nav button {
  color: black;
  background: #FFFFFF;
  border: 0px solid #e5e7e9;
  border-radius: 7px;
  font-size: 18px;
  font-weight: 500;
}

footer {
  height: 13vh;
  min-height: 100px;
  background-color: #BCDDFE;
  gap: 2rem;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border: 0px solid #FF2850;
}

.footer-text {
  display: flex;
  justify-content: space-between;
  align-items: top;
  margin: 0 0 1rem 0;
  gap: 2rem;
  font-size: 18px;
  border: 0px solid #FF2850;
}

.col1 .footer-link {
  display:inline-block;
  text-decoration: none;
  color:  black;
  border: 0px solid #FF2850;
}

.col2 .footer-link {
  display:inline-block;
  text-decoration: none;
  color:  black;
  border: 0px solid #FF2850;
}

el-dropdown {
  border: 0px solid #FF2850;
}

</style>
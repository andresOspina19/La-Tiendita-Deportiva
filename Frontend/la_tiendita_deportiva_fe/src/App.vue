<template>
  <div id="mi-app" class="app">
    <header>
      <div class = "logo">
        <img src = "@/assets/logo.svg" v-on:click="loadHome"/>
      </div>
      <div>
        <nav class="nav-option">
          <div class="search">
            <form v-on:submit.prevent="searchProducts" class="search-products">
              <input type="text" class="form-control" v-model="search" placeholder="Buscar"/>
              <button type="submit" class="search-button"> <img src = "@/assets/search.jpg" class="search-img"> </button>
            </form>
          </div>
          <button v-on:click="loadProducts">Productos</button>
          <!--<button v-if="!is_auth" v-on:click="loadLogIn"><img src="@/assets/user.svg"/></button>-->
          <button v-if="!is_auth" v-on:click="loadLogIn">Iniciar Sesión</button>
          <button v-if="!is_auth" v-on:click="loadSignUp">Registrarse</button>
          <div id="cart" v-if="is_auth">
            <span v-if="is_auth" id="nav-cart-count">{{ Object.keys(getUserCart.cartItems).length }}</span>
            <button v-if="is_auth" v-on:click="loadCart"><img src="@/assets/cart.svg"/></button>
          </div>
          <button v-if="is_auth" v-on:click="loadOrders">Pedidos</button>
          <button v-if="is_auth" v-on:click="logOut">Cerrar Sesión</button>
        </nav>
      </div>
    </header>

    <main>
      <router-view
        v-on:completedLogIn="completedLogIn"
        v-on:completedSignUp="completedSignUp"
        v-on:logOut="logOut"
        v-on:refreshCartCount="refreshCartCount"
      >
      </router-view>
    </main>

    <footer>
      <div class = "logo">
        <img id="footer-img" src = "@/assets/logo2.svg">
      </div>
      <div class="footer-text">
        <div class="col1">
              <h3 class="text-light font-weight-bold pb-2">Puedes también:</h3>
              <a class="footer-link font-weight-light" href="#" v-on:click="loadLogIn">Iniciar sesión</a><br><br>
              <a class="footer-link font-weight-light" href="#" v-on:click="loadSignUp">Registrarte</a><br><br>
              <a class="footer-link font-weight-light" href="#" v-on:click="loadproducts">Conoce nuestros productos</a><br><br>
        </div>

        <div class="col2">
              <h3 class="text-light font-weight-bold">Contáctanos</h3>
              <a class="footer-link font-weight-light" href="#">Llamanos al:</a><br><br>
              <a class="footer-link font-weight-light" href="#">(+57 555-5555)</a><br><br>
              <a class="footer-link font-weight-light" href="#">O escribenos al:</a><br><br>
              <a class="footer-link font-weight-light" href="#">latienditadeportiva@gmail.com</a><br><br>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
/*import AppDropdown from "./components/AppDropdown.vue";
import AppDropdownContent from "./components/AppDropdownContent.vue";
import AppDropdownItem from "./components/AppDropdownItem.vue";*/
import gql from "graphql-tag";
import Swal from 'sweetalert2';

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
        return this.$route.meta.is_auth;
      },
      set: function () {},
    }
  },
  data: function() {
    return {
      search: null,
      getUserCart: {
        cartItems: []
      },
    };
  },
  methods: {
    loadOrders: function () {
      this.$router.push({ name: "orders" });
    },
    loadCart: function () {
      this.$router.push({ name: "cart" });
    },
    refreshCartCount: function() {
      this.$apollo.queries.getUserCart.refetch();
    },
    loadProducts: function () {
      this.$router.push({ name: "products" });
    },
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
      Swal.fire({
        text: "Autenticación Exitosa",
        icon: "success"
      });
      this.loadHome();
    },
    completedSignUp: function (data) {
      Swal.fire({
        text: "Registro Exitoso",
        icon: "success"
        })
      this.completedLogIn(data);
    },
    logOut: function () {
      localStorage.clear();
      Swal.fire({
        text: "Sesión Cerrada",
        icon: "success"
      });
      this.loadLogIn();
    },
    searchProducts: function () {
      this.$router.push({name: 'products', query: {search: this.search, page: 0}});
    },
  },

  apollo: {
        getUserCart: {
            query: gql`
                query ($username: String!) {
                    getUserCart(username: $username) {
                        cartItems {
                        product {
                            productId
                            productName
                            imageURL
                            price
                            stock
                            description
                            category
                            sales
                        }
                        id
                        quantity
                        }
                        totalCost
                    }
                }
            `,
            variables() {
                return {
                    username: localStorage.getItem("username") || "none"
                }
            },
            skip() {
              return !this.$route.meta.is_auth;
            }
        }
    },
    mounted: function() {
      this.$apollo.queries.getUserCart.refetch();
    },
    created: function () {
      this.$apollo.queries.getUserCart.refetch();
    }  
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

button.search-button {
    border-radius: 10px;
    padding: 0rem;
    display: flex;
    margin-right: 1.6rem;
    align-content: center;
    justify-content: center;
    align-items: center;
}

form.search-products {
    display: flex;
    flex-direction: row;
    align-items: center;
}

img.search-img {
    height: 1.5rem;
    width: 1.5rem;
}

header {
  display: flex;
  align-items: center;
  height: 8vh;
  justify-content: space-between;
  border: 0px solid #283747;
  font-family:  comfortaa;
  font-size: 100%;
}

header .nav-option {
  display: flex;
  align-items:  center;
  padding: 10px;
  border: 0px solid #283747;
}

header nav button {
    background: #FFFFFF;
    border: 0px solid;
    border-radius: 7px;
    padding: 5px 5px 5px 5px;
    margin: 2px 7px 2px 4px;
    font-family: comfortaa;
    font-size: 100%;
}
.search{
    border-radius: 10px;
    height: auto;
    font-family: comfortaa;
    font-size: 100%;
}
.form-control{
      border-radius: 10px;
    height: auto;
    font-family: comfortaa;
    font-size: 100%;
    margin: 0px 0px 0px 0px;
    padding: 3px 0px 3px 10px;
    border-color: #575353a3;
    background-color: #f1efef66;
}
footer {
  height: auto;
    min-height: auto;
    background: linear-gradient(45deg, #BCDDFE, #25232352);
    gap: 2rem;
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 1px 0px 1px 0px;
    margin: 20px 0px 10px 0px;
}
@import url('https://fonts.googleapis.com/css2?family=Comfortaa:wght@300&display=swap');
.footer-text {
  display: flex;
  justify-content: space-between;
  align-items: top;
  margin: 0 0 1rem 0;
  gap: 2rem;
  font-size: 18px;
  border: 0px solid #FF2850;
  font-family:  comfortaa;
    font-size: 100%;
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

#nav-cart-count {
  background-color: #5271FF;
  color: white;
  border-radius: 50%;
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  font-size: 16px;
  margin-left: 25px;
  margin-top: 10px;
}

el-dropdown {
  border: 0px solid #FF2850;
}
</style>
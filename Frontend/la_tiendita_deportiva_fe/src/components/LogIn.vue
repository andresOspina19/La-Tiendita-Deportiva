<template>
    <div class="logIn_user">
        <div class="container_logIn_user">
            <h2>Iniciar sesión</h2>
            <form v-on:submit.prevent="processLogInUser" >
                <input type="text" v-model="user.username" placeholder="Usuario">
                <br>
                <input type="password" v-model="user.password" placeholder="Contraseña">
                <br>
                <button type="submit">Iniciar Sesión</button>
            </form>
        </div>
    </div>
</template>

<script>
import gql from "graphql-tag";
import Swal from 'sweetalert2';

export default {
  name: "LogIn",

  data: function() {
    return {
      user: {
        username: "",
        password: "",
      },
    };
  },

  methods: {
    processLogInUser: async function() {
      await this.$apollo
        .mutate({
          mutation: gql`
            mutation($credentials: CredentialsInput!) {
              logIn(credentials: $credentials) {
                refresh
                access
              }
            }
          `,
          variables: {
            credentials: this.user,
          },
        })
        .then((result) => {
          let dataLogIn = {
            username: this.user.username,
            token_access: result.data.logIn.access,
            token_refresh: result.data.logIn.refresh,
          };

          this.$emit("completedLogIn", dataLogIn);
        })
        .catch((error) => {
            Swal.fire({
              text: "Las Credenciales son Incorrectas",
              icon: "error"
            });
        });
    },
  },
}
</script>

// pendiente por modificar
<style>
@import url('https://fonts.googleapis.com/css2?family=Comfortaa:wght@300&display=swap');
    .logIn_user{
        margin: 0;
        padding: 0%;
        height: 100%;
        width: 100%;  
        display: flex;
        justify-content: center;
        align-items: center;
        font-family:  comfortaa;
        font-size: 100%;
    }

    .container_logIn_user {
        border: 3px solid  #283747;
        border-radius: 10px;
        width: auto;
        height: auto;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

    .logIn_user h2{
        color: #283747;

    }

    .logIn_user form{
        width: 70%;
        
    }

    .logIn_user input{
        height: auto;
        width: 100%;
        font-family: comfortaa;
        font-size: 100%;
        box-sizing: border-box;
        padding: 10px 20px;
        margin: 5px 0;
        border: 1px solid #283747;
        border-radius: 20px;
    }

    .logIn_user button{
        width: 100%;
        height: 100%;
        font-family: comfortaa;
        font-size: 100%;
        color: #E5E7E9;
        background: #283747;
        border: 1px solid #E5E7E9;
        border-radius: 18px;
        padding: 10px 25px;
        margin: 5px 0;
    }

    .logIn_user button:hover{
        color: #E5E7E9;
        background: rgb(137, 176, 228);
        border: 1px solid #283747;
    }

</style>
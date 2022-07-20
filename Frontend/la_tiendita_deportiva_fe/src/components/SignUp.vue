<template>

    <div class="signUp_user">
        <div class="container_signUp_user">
            <h2>Registrarse</h2>

            <form v-on:submit.prevent="processSignUp" >
                <input type="text" v-model="user.username" placeholder="Usuario">
                <br>
                
                <input type="password" v-model="user.password" placeholder="Contraseña">
                <br>
                
                <input type="text" v-model="user.name" placeholder="Nombre">
                <br>

                <input type="email" v-model="user.email" placeholder="Correo">
                <br>

                <input type="text" v-model="user.address" placeholder="Dirección">

                <input type="number" v-model="user.phoneNumber" placeholder="Número de télefono">
                <br>

                <button type="submit">Registrarse</button>
            </form>
        </div>

    </div>

</template>


<script>
import gql from "graphql-tag";
import Swal from 'sweetalert2';

export default {
    name: "SignUp",

    data: function() {
        return {
        user: {
            username: "",
            password: "",
            name: "",
            email: "",
            address: "",
            phoneNumber: "",
          },
        };
    },

  methods: {
    processSignUp: async function() {
      this.user.phoneNumber = this.user.phoneNumber + "";
      await this.$apollo
        .mutate({
          mutation: gql`
            mutation($userInput: SignUpInput!) {
              signUpUser(userInput: $userInput) {
                refresh
                access
              }
            }
          `,
          variables: {
            userInput: this.user,
          },
        })
        .then((result) => {
          console.log("entro")
          let dataLogIn = {
            username: this.user.username,
            token_access: result.data.signUpUser.access,
            token_refresh: result.data.signUpUser.refresh,
          };
        console.log("entro2")
          this.$emit("completedSignUp", dataLogIn);
        })
        .catch((error) => {
          Swal.fire({
              text: "No se pudo realizar el registro.  Por favor, inténtelo de nuevo.",
              icon: "error"
            });
        });
    },

  },
}
</script>

//Pendiente por modificar
<style>
@import url('https://fonts.googleapis.com/css2?family=Comfortaa:wght@300&display=swap');
    .signUp_user{
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

    .container_signUp_user {
        border: 3px solid  #283747;
        border-radius: 10px;
        width: auto;
        height: auto;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        font-family:  comfortaa;
        font-size: 100%;
    }

    .signUp_user h2{
        color: #283747;

    }

    .signUp_user form{
        width: 70%;
        
    }

    .signUp_user input{
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

    .signUp_user button{
        width: 100%;
        height: 40px;
         color: #E5E7E9;
        background: #283747;
        border: 1px solid #E5E7E9;
        border-radius: 18px;
        padding: 10px 25px;
        margin: 5px 0 25px 0;
        font-family:  comfortaa;
        font-size: 100%;
    }

    .signUp_user button:hover{
         color: #E5E7E9;
        background: rgb(137, 176, 228);
        border: 1px solid #283747;
    }
</style>
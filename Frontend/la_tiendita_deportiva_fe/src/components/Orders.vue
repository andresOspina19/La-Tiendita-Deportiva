<template>
    <div class="User_info">
        <div class="container_user_info">
            <h2>Datos de Usuario</h2>
                <strong>Username: </strong><span>{{userDetailById.username}}</span><br>
                <strong>Nombre: </strong><span>{{userDetailById.name}}</span><br>
                <strong>Email: </strong><span>{{userDetailById.email}}</span><br>
                <strong>Dirección: </strong><span>{{userDetailById.address}}</span><br>
                <strong>Celular: </strong><span>{{userDetailById.phoneNumber}}</span><br>
                <strong>Estado: </strong><span>{{userDetailById.is_active}}</span><br>
        </div>
        <div class="container_user_pays" >
            <h2 >Datos de transacciones</h2>
            <div v-for="transaccion in getAllOrdersByUsername" :key="transaccion.orderId">
                
                <strong>orderId: </strong><span>{{transaccion.orderId}}</span><br>
                <strong>Token de pago: </strong><span>{{transaccion.paymentToken}}</span><br><br>
                <button class="ir_a_pago" type="submit" v-on:click="goToOrderdetail(transaccion.orderId)"><strong>Detalle de pedido</strong></button><br><br>
                <strong>Fecha de creación: </strong><span>{{transaccion.createdDate}}</span><br>
                
            <div v-for="order in transaccion.orderProducts" :key="order.orderId">
                <strong>Product Id: </strong><span>{{ order.product.productId}}</span><br>
                <strong>Producto: </strong><span>{{ order.product.productName}}</span><br>
                <strong>Producto price: </strong><span>{{ order.product.price}}</span><br>
                <strong>Cantidad solicitadas: </strong><span>{{ order.quantity}}</span><br>
                <strong>Cantidad solicitadas: </strong><span>{{ order.price}}</span><br><br>
                
            </div>    
            </div>
        </div>
    </div>
</template>


<script>
import gql from "graphql-tag";
import Swal from 'sweetalert2';
import jwt_decode from "jwt-decode";

export default {
    name: "orders",

    data: function() {
    return {
            userId: jwt_decode(localStorage.getItem("token_refresh")).user_id,
            username: localStorage.getItem("username") || "none",
            userDetailById: [],
            getAllOrdersByUsername: {
                orderId: "",
                paymentToken: "",
                createdDate: "",
            },
        };
    },
    methods: {
        goToOrderdetail: function (orderId) {
            this.$router.push({ name: 'Orderdetail', query: { id: orderId }});
        },
    },
    apollo: {
    getAllOrdersByUsername: {
      query: gql`
        query ($username: String!) {
          getAllOrdersByUsername(username: $username) {
                orderId
                username
                paymentToken
                createdDate
                totalPrice
                orderProducts {
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
                price
                quantity
                createdDate
                }
          }
        }
      `,
        variables() {
            return {
            username: this.username,
            };
        },
    },
    userDetailById: {
      query: gql`
        query ($userId: Int!) {
          userDetailById(userId: $userId) {
            username
            name
            email
            address
            phoneNumber
            is_active
          }
        }
      `,
        variables() {
            return {
            userId: this.userId,
            };
        },
        },
    },
};
</script>

//Pendiente por modificar
<style>

  @import url('https://fonts.googleapis.com/css2?family=Comfortaa:wght@300&display=swap');
  .User_info{
    padding: 10px; 
    float: center; 
    max-width: 90%; 
    text-align: justify; 
    font-size: 20px;
    font-family:  comfortaa;
    font-size: 100%;
  }
  .container_user_info{
    padding: 10px; 
    float: left; 
    max-width: 45%; 
    text-align: justify; 
    font-size: 20px;
    font-family:  comfortaa;
    font-size: 100%;
  }
  .container_user_pays{
    padding: 10px; 
    float: right; 
    max-width: 45%; 
    text-align: justify; 
    font-size: 20px;
    font-family:  comfortaa;
    font-size: 100%;
  }
  .ir_a_pago{
    background: #2079b0;
    background-image: -webkit-linear-gradient(top, #2079b0, #eb94d0);
    background-image: -moz-linear-gradient(top, #2079b0, #eb94d0);
    background-image: -ms-linear-gradient(top, #2079b0, #eb94d0);
    background-image: -o-linear-gradient(top, #2079b0, #eb94d0);
    background-image: linear-gradient(to bottom, #2079b0, #eb94d0);
  }
</style>
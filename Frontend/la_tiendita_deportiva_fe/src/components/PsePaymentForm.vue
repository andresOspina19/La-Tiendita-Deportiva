<template>
    <div class="container" v-show="!isPaying">
        <div class="container-form">
            <img src="@/assets/pse.png">
            <h2>PAGO PSE</h2>
            <h3>Monto a pagar:  <strong id="price">{{this.convertToCOP(getUserCart.totalCost)}} COP</strong></h3>
            <form v-on:submit.prevent="processKushkiPsePayment" >
                <select v-model="paymentForm.documentType" required>
                    <option value="CC">Cédula de ciudadanía</option>
                    <option value="TI">Tarjeta de identidad</option>
                    <option value="CE">Cédula de extranjería</option>
                    <option value="NIT">NIT</option>
                    <option value="PP">Pasaporte</option>
                </select>
                <br>
                <input type="number" v-model="paymentForm.documentNumber" placeholder="No. de documento" min="100000" required>
                <br>
                <input type="email" v-model="paymentForm.email" placeholder="Correo" required>
                <br>
                <select v-model="paymentForm.userType" required>
                    <option value="0">Persona Natural</option>
                    <option value="1">Persona Jurídica</option>
                </select>
                <br>
                <select v-model="paymentForm.bankId" required>
                    <option v-for="bank in kushkiBanks" :key="bank.code" :value="bank.code">{{bank.name}}</option>
                </select>
                <br>
                <button type="submit" :disabled='isPaying'>Pagar</button>
            </form>
        </div>
    </div>
    <div v-show="isPaying" class="paying">
        <h2>Por favor espere</h2>
        <div class="lds-ring"><div></div><div></div><div></div><div></div></div>
    </div>
</template>

<script>
import gql from "graphql-tag";
import axios from 'axios';
import Swal from 'sweetalert2';

export default {
    name: "PsePaymentForm",
    data: function() {
        return {
            username: localStorage.getItem("username") || "none",
            getUserCart: Object,
            paymentForm: {
                bankId: "0",
                amount: {
                    subtotalIva: 0,
                    subtotalIva0: 0,
                    iva: 0
                },
                callbackUrl: "https://latienditadeportiva.herokuapp.com/orders",
                userType: "0",
                documentType: "CC",
                documentNumber: "",
                paymentDescription: "Pago de un pedido",
                email: "",
                currency: "COP"
            },
            kushkiBanks: [],
            paymentToken: Object,
            isPaying: false,
        }
    },
    methods: {
        processKushkiPsePayment: async function(){
            if (this.paymentForm.bankId === "0") {
                Swal.fire({
                    text: "Por favor, seleccione un banco",
                    icon: "error",
                });
            } else if (this.getUserCart.totalCost > 0) {
                this.isPaying = true;
                await this.getPaymentToken();
            } else {
                 Swal.fire({
                    text: "Primero debes tener productos en el carrito",
                    icon: "error",
                });
                this.$router.push({ name: "products" });
            }
        },

        getPaymentToken: async function() {
            this.paymentForm.amount.subtotalIva0 = this.getUserCart.totalCost;
            this.paymentForm.documentNumber = this.paymentForm.documentNumber + "";
            var axios = require('axios');
            var data = JSON.stringify(this.paymentForm);

            var config = {
            method: 'post',
            url: 'https://api-uat.kushkipagos.com/transfer/v1/tokens',
            headers: { 
                'content-type': 'application/json', 
                'public-merchant-id': '6e28f9162aad4b48946f01b5cad17e78'
            },
            data : data
            };

            axios(config)
            .then((response) => {
                this.paymentToken = response.data.token;

                this.initPayment();
            })
            .catch((error) => {
                this.isPaying = false;
                Swal.fire({
                    text: "Por favor, intentelo más tarde",
                    icon: "error"
                });
            });
        },
        initPayment: async function(){
            await this.$apollo.mutate({
                mutation: gql`
                    mutation ($payment: Payment!) {
                        initPayment(payment: $payment) {
                            redirectUrl
                            transactionReference
                        }
                    }
                `,

                variables: {
                    payment: {
                        token: this.paymentToken,
                        username: this.username,
                        amount: this.paymentForm.amount
                    }
                },
            })
            .then((result) => {
                let initPaymentData = {
                    redirectUrl: result.data.initPayment.redirectUrl,
                    transactionReference: result.data.initPayment.transactionReference
                };
                
                //Redireccionamos a la url de Kushki
                window.location.replace(initPaymentData.redirectUrl);
            })
            .catch((error) => {
                Swal.fire({
                    text: error,
                    icon: "error"
                });
                this.isPaying = false;
            });

        },

        getKushkiBanks: async function() {
            var config = {
                method: 'get',
                url: 'https://api-uat.kushkipagos.com/transfer/v1/bankList',
                headers: { 
                    'Public-Merchant-Id': '6e28f9162aad4b48946f01b5cad17e78'
                }
            };

            axios(config)
            .then((response) => {
                this.kushkiBanks = response.data;
            })
            .catch((error) => {
                Swal.fire({
                    text: "Por favor, intentelo más tarde",
                    icon: "error"
                });
                this.$router.push({ name: "cart" });
            });
        },
        convertToCOP: function (precio) {
            const formatterPeso = new Intl.NumberFormat('es-CO', {
                    style: 'currency',
                    currency: 'COP',
                    minimumFractionDigits: 0
            });

            return formatterPeso.format(precio);
        }
    },
    apollo: {
        getUserCart: {
            query: gql`
                query Query($username: String!) {
                    getUserCart(username: $username) {
                        totalCost
                    }
                }
            `,
            variables() {
                return {
                    username: this.username
                }
            }
        }
    },

    created: function () {
        this.getKushkiBanks();
        this.$apollo.queries.getUserCart.refetch();
    }
}
</script>

<style>

.container-form {
        border-radius: 50px;
        background: #ffffff;
        box-shadow:  20px 20px 60px #d9d9d9,
             -20px -20px 60px #ffffff;
        padding: 2rem;
        width: 50%;
        height: auto;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        font-family:  comfortaa;
        font-size: 100%;
    }

    .container-form h2{
        color: #283747;
    }

    .container-form form{
        width: 100%;
    }

    .container-form select {
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

    .container-form input{
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

    .container-form button{
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

    .container-form button:hover{
         color: #E5E7E9;
        background: rgb(137, 176, 228);
        border: 1px solid #283747;
    }

    .paying {
        display: flex;
        flex-direction: column;
        flex-wrap: nowrap;
        align-items: center;
        align-content: center;
        justify-content: center;
        height: 36rem;
        width: 100%;
    }

    .paying h2 {
        color: rgb(57, 134, 235);
    }

    .lds-ring {
        width: 80px;
        height: 80px;
    }
    .lds-ring div {
        box-sizing: border-box;
        display: block;
        position: absolute;
        width: 64px;
        height: 64px;
        margin: 8px;
        border: 8px solid rgb(57, 134, 235);
        border-radius: 50%;
        animation: lds-ring 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;
        border-color: rgb(57, 134, 235) transparent transparent transparent;
    }
    .lds-ring div:nth-child(1) {
        animation-delay: -0.45s;
    }
    .lds-ring div:nth-child(2) {
        animation-delay: -0.3s;
    }
    .lds-ring div:nth-child(3) {
        animation-delay: -0.15s;
    }
    @keyframes lds-ring {
    0% {
        transform: rotate(0deg);
    }
    100% {
        transform: rotate(360deg);
    }
    }
</style>
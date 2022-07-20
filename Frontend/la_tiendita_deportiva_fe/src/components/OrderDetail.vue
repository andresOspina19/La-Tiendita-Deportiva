<template> 
    <div class="container">

        <div class= "information">

            <div class="container-payed">
                <h1 class= "price" id="order-total-price">PRECIO TOTAL PAGADO: {{this.convertToCOP(getOrderById.totalPrice)}}</h1>
                <button id="go-to-kushki" v-on:click="goToKushki(getPaymentByToken.bankurl)"><strong>Ver pago en Kushki Pagos PSE</strong></button><br><br>
            </div>

            <div class= "product-o">
                <div v-for="order in getOrderById.orderProducts" :key="order.orderId" class="card-o">
                    <img class="productImg-o" :src="order.product.imageURL">
                    <div class= "right-side">

                        <h1>
                            {{order.product.productName}}
                        </h1>

                        <h2 class="price">
                            Pagado: {{this.convertToCOP( (order.price * order.quantity) )}} COP
                        </h2>

                        <h4>
                            Unidades pedidas: {{order.quantity}}
                        </h4> 

                        <h3>
                            Precio individual: {{this.convertToCOP(order.product.price)}} COP
                        </h3> 

                    </div>   
                </div>
            </div>  
            <div class="description">
                <h1>Descripción</h1>
                <div v-for="order in getOrderById.orderProducts" :key="order.orderId">
                    <ul>
                        <li>{{order.product.description}}</li>
                    </ul>
                </div>
            </div>
            


        </div>
    </div>  
</template>

<script>
import gql from "graphql-tag";
import Swal from 'sweetalert2';

export default {
    name: "Orderdetail",

    data: function() {
        return {
            username: localStorage.getItem("username") || "none",
            /*orderId: localStorage.getItem("orderId") || "none",*/
            getOrderById: Object,
            getPaymentByToken: Object,
        };
    },
    methods: {
        convertToCOP: function (precio) {
            const formatterPeso = new Intl.NumberFormat('es-CO', {
                    style: 'currency',
                    currency: 'COP',
                    minimumFractionDigits: 0
            });

            return formatterPeso.format(precio);
        },
        goToKushki: function(url) {
          //Redireccionamos a la url de Kushki
          window.location.replace(url);
        },
    },

    apollo: {
        getOrderById: {
            query: gql`
                query ($orderByIdInput: GetOrderByIdInput) {
                    getOrderById(orderByIdInput: $orderByIdInput) {
                        orderId
                        username
                        paymentToken
                        createdDate
                        totalPrice
                        orderProducts {
                            product {
                                productName
                                imageURL
                                sales
                                category
                                description
                                stock
                                price
                                productId
                            }
                        quantity
                        createdDate
                        price
                        }
                    }
                }
            `,
            variables() {
                return {
                    orderByIdInput: {
                        orderId: this.$route.query.id,
                        username: this.username,
                    }
                }
            }
        },

        getPaymentByToken: {
            query: gql`
                query ($paymentByTokenInput: PaymentByTokenInput!) {
                    getPaymentByToken(paymentByTokenInput: $paymentByTokenInput) {
                        bankurl
                    }
                }
            `,
            variables() {
                return {
                    paymentByTokenInput: {
                        username: this.username,
                        token: this.$route.query.token,
                    }
                }
            }
        }
    },

    created: function () {
        this.$apollo.queries.getOrderById.refetch();
        this.$apollo.queries.getPaymentByToken.refetch();
    },
}
</script>

<style>
#order-total-price {
    text-align: center;
}

#go-to-kushki {
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

.container {
    display: grid;
    justify-items: center;
    margin: 3rem 0rem;
}

.information {
    width: 90%;
    border-radius: 50px;
    background: #ffffff;
    box-shadow:  20px 20px 60px #d9d9d9,
             -20px -20px 60px #ffffff;
}

.product-o {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(10rem, 30rem));
    align-content: center;
    justify-items: center;
    justify-content: center;
    align-items: stretch;
}

.productImg-o {
    border-radius: 60px;
    height: 300px;
    width: 350px;
    object-fit: cover;
    margin: 1rem;
}

#price {
    color: rgba(64, 191, 255, 1);
}

form.formAddToCart {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-around;
}

.quantity {
    display: inline-flex;
    flex-direction: row;
    align-content: center;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: center;
}

form.formAddToCart button {
    background: #ebf3fc ;
    border: 1px solid #33A0FF;
}

form.formAddToCart button strong {
    color: #33A0FF;
}

.description {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 0rem 0rem 1rem 0rem;
}

.right-side {
    margin: 2rem;
    display: flex;
    flex-direction: column;
    align-content: center;
    flex-wrap: nowrap;
    justify-content: center;
    align-items: flex-start;
}

.card-o {
    margin: 2rem;
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    align-content: center;
    justify-content: center;
    align-items: center;
    border-radius: 50px;
    background: #ffffff;
    box-shadow:  20px 20px 60px #d9d9d9,
             -20px -20px 60px #ffffff;
}

.container-payed {
    display: flex;
    flex-direction: column;
    justify-content: center;
    flex-wrap: nowrap;
    align-items: center;
}
</style>    
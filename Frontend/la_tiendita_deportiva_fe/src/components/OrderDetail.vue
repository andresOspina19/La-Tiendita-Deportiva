<template> 
    <div class="container">

        <div class= "information">
            <div class= "product">
                <div v-for="order in getOrderById.orderProducts" :key="order.orderId">
                    <img class="productImg" :src="order.product.imageURL">
                    <div class= "right-side">
                        <h4>
                            Vendidos: {{order.product.sales}}
                        </h4>
                        
                        <h1>
                            {{order.product.productName}}
                        </h1>

                        <h4>
                            Categoría: {{order.product.category}}
                        </h4>

                        <h2 id="price">
                            {{getOrderById.totalPrice}} COP
                        </h2> 
                        <h3>
                            Precio individual: {{order.product.price}} unidades
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
        };
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
                    /*orderId: this.$route.query.id*/
                    orderByIdInput: {
                        orderId: this.$route.query.id,
                        username: this.username,
                    }
                }
            }
        }
    }
}
</script>

<style>
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

.product {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(10rem, 30rem));
    align-content: center;
    justify-items: center;
    justify-content: center;
    align-items: stretch;
}

.productImg {
    border-radius: 60px;
    height: 300px;
    width: 350px;
    object-fit: contain;
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
    align-content: flex-start;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: stretch;
}
</style>    
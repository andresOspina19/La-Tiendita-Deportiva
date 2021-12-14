<template> 
    <div class="container">
        <table class="table">
            <thead>
                <tr>
                    <th>PRODUCTO</th>
                    <th>PRECIO</th>
                    <th>CANTIDAD</th>
                    <th>PRECIO UNITARIO</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item,index) in getUserCart.cartItems" :key="item.product.productId">
                    <td>
                        <div class= "container-product">
                            <button class="delete-item-button" @click="processDeleteCartItem(item.id)">X</button>
                            <div class= "container-images"> 
                                <img class="item-image" :src="item.product.imageURL">
                            </div>
                            {{item.product.productName}}
                        </div>
                    </td>
                    <td> {{ this.convertToCOP( (item.product.price * item.quantity) ) }}</td>
                    <td>
                        <input type="number" v-model="eachQuantity[index]" min="1" :max="item.product.stock" @change="processUpdateCart(index, item.id, item.product.productId)" required>
                    </td>
                    <td>{{ this.convertToCOP( item.product.price ) }}</td>
                </tr>
            </tbody>
        </table>
        <div class="checkout">
            <div class="total">
                <strong>TOTAL</strong>           <strong>{{this.convertToCOP( getUserCart.totalCost )}} COP</strong>
            </div>
            <button id="checkout-button" v-on:click="loadPsePaymentForm">Pagar</button>
        </div>
    </div>  
</template>

<script>
import gql from "graphql-tag";
import Swal from 'sweetalert2';

export default {
    name: "Cart",

    data: function() {
        return {
            username: localStorage.getItem("username") || "none",
            getUserCart: Object,
            quantities: [],
            forceReload: 0
        };
    },

    computed: {
        cartItems: {
            get: function () {
                return this.getUserCart.cartItems;
            },
            set: function () {},
        },
        eachQuantity: {
            get: function () {
                return this.obtainQuantities();
            },
            set: function () {},
        }
    },

    methods: {
        loadPsePaymentForm: function () {
            if (this.getUserCart.totalCost == 0) {
                Swal.fire({
                    text: "Primero debes tener productos en el carrito",
                    icon: "error"
                });
            } else {
                this.$router.push({ name: "psePaymentForm" });
            }
        },

        convertToCOP: function (precio) {
            const formatterPeso = new Intl.NumberFormat('es-CO', {
                    style: 'currency',
                    currency: 'COP',
                    minimumFractionDigits: 0
            });

            return formatterPeso.format(precio);
        },

        obtainQuantities: function () {
            let eachQuantityy = [];

            for (let i = 0; i < this.cartItems.length; i++) {
                let cartItem = this.cartItems[i];
                eachQuantityy.push(cartItem.quantity);
            }
            return eachQuantityy;
        },

        processUpdateCart: async function(index, cartItemId, productId) {
            if (this.eachQuantity[index] >= 1) {
                await this.$apollo.mutate({
                mutation: gql`
                    mutation ($updateCartItem: UpdateItemInput!) {
                        updateCartItem(updateCartItem: $updateCartItem) {
                            success
                            message
                            timestamp
                        }
                    }
                `,

                variables: {
                    updateCartItem: {
                        id: cartItemId,
                        productId: productId,
                        quantity: this.eachQuantity[index],
                        username: this.username
                    }
                },
            })
            .then((result) => {
                    let dataUpdateCart = {
                        success: result.data.updateCartItem.success,
                        message: result.data.updateCartItem.message,
                    };

                    this.$apollo.queries.getUserCart.refetch();
                })
                .catch((error) => {
                    if ( (error + "").includes("409")) {
                        Swal.fire({
                            text: "No puedes añadir tantos productos",
                            icon: "error"
                        });
                    } else {
                        this.$router.push({ name: "logIn" });
                    }
                });
            }
        },

        processDeleteCartItem: async function (cartItemId) {
            await this.$apollo.mutate({
                mutation: gql`
                    mutation ($deleteItemInput: DeleteItemInput) {
                        deleteCartItem(deleteItemInput: $deleteItemInput) {
                            success
                            message
                            timestamp
                        }
                    }
                `,

                variables: {
                    deleteItemInput: {
                        cartItemId: cartItemId,
                        username: this.username
                    }
                },
            })
                .then((result) => {
                    let dataDeleteCartItem = {
                        success: result.data.deleteCartItem.success,
                        message: result.data.deleteCartItem.message,
                    };

                    this.getUserCart = {};
                    this.$apollo.queries.getUserCart.refetch();
                })
                .catch((error) => {
                    if ( (error + "").includes("409")) {
                        Swal.fire({
                            text: "No se pudo eliminar el producto del carrito",
                            icon: "error"
                        });
                    } else {
                        this.$router.push({ name: "logIn" });
                    }
                });


        }
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
                    username: this.username
                }
            }
        }
    },

    created: function () {
        this.$apollo.queries.getUserCart.refetch();
    }
}
</script>

<style>
.container {
    display: grid;
    justify-items: center;
    margin: 3rem 0rem;
}

.table {
    text-align: center;
    border-collapse: collapse;
}

.table th, td {
    padding: 2.5rem;
}

.table td {
    border-bottom: solid 3px rgba(246, 247, 248, 1);
}

.table thead {
    border-bottom: solid 3px rgba(246, 247, 248, 1);
}

.container-product {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-direction: row;
}

.delete-item-button {
    color: #FF6875;
    background: #fcf4fc;
    border: none;
    width: 1.5rem;
    height: 1.5rem;
    text-align: center;
    border-radius: 360px;
}

.container-images {
    width: 4rem;
    height: 3rem;
}

.container-images img {
    min-height:100%;
    min-width:100%;
    max-height:100%;
    max-width:100%;
    object-fit: cover;
}

.checkout {
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    align-content: center;
    justify-content: center;
}

.total {
    display: flex;
    justify-content: space-between;
    margin: 1rem 0rem;
}

#checkout-button {
    background: #33A0FF;
    color: rgba(255, 255, 255, 1);
    width: 20rem;
    height: 2rem;
    border: none;
}

</style>    
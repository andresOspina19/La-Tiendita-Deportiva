<template> 
    <div class="container">
        <div class= "information">
            <div class= "product">
                <img class="productImg" :src="productByproductId.imageURL">
                <div class= "right-side">
                    <h4>
                        Vendidos: {{productByproductId.sales}}
                    </h4>
                    
                    <h1>
                        {{productByproductId.productName}}
                    </h1>

                    <h4>
                        Categoría: {{productByproductId.category}}
                    </h4>

                    <h2 id="price">
                        {{precioCOP}} COP
                    </h2> 
                    <h3>
                        Stock disponible: {{productByproductId.stock}} unidades
                    </h3> 

                    <form class="formAddToCartt" v-on:submit.prevent="processAddToCart">
                        <div class="quantity">
                            <pre>Cantidad: </pre>
                            <input type="number" v-model="cantidad" min="1" :max="productByproductId.stock">
                        </div>
                        <button type="submit"><strong>Agregar al carrito</strong></button>
                    </form>
                </div>   
            </div>  
            <div class="description">
                <h1>Descripción</h1>

                <p class="description-text">
                    {{productByproductId.description}}
                </p>
            </div>
            


        </div>
    </div>  
</template>

<script>
import gql from "graphql-tag";
import Swal from 'sweetalert2';

export default {
    name: "ProductDetail",

    data: function() {
        return {
            username: localStorage.getItem("username") || "none",
            productByproductId: Object,
            cantidad: 1,
        };
    },

    computed: {
        precio: {
            get: function () {
                return this.productByproductId.price;
            },
            set: function () {},
        },
        precioCOP: {
            get: function () {
                
                return this.convertToCOP();
                
            },
            set: function () {}
        }
    },

    methods: {
        convertToCOP: function () {
            const formatterPeso = new Intl.NumberFormat('es-CO', {
                    style: 'currency',
                    currency: 'COP',
                    minimumFractionDigits: 0
            });

            return formatterPeso.format(this.precio);
        },
        processAddToCart: async function() {
            await this.$apollo.mutate({
                mutation: gql`
                    mutation ($addToCartInput: AddToCartInput) {
                        addCartItem(addToCartInput: $addToCartInput) {
                            success
                            message
                            timestamp
                        }
                    }
                `,

                variables: {
                    addToCartInput: {
                        username: this.username,
                        quantity: this.cantidad,
                        productId: this.productByproductId.productId
                    }
                },
            })
                .then((result) => {
                    let dataAddToCart = {
                        success: result.data.addCartItem.success,
                        message: result.data.addCartItem.message,
                    };

                    Swal.fire({
                        text: dataAddToCart.message,
                        icon: "success"
                    });
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
        },
    },

    apollo: {
        productByproductId: {
            query: gql`
                query ($productId: String!) {
                    productByproductId(productId: $productId) {
                        productId
                        productName
                        imageURL
                        price
                        stock
                        description
                        category
                        sales
                    }
                }
            `,
            variables() {
                return {
                    productId: this.$route.query.id
                }
            }
        }
    },

    created: function () {
        this.$apollo.queries.productByproductId.refetch();
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
    justify-items: center;
    justify-content: center;
    align-items: center;
    align-content: center;
}

.productImg {
    border-radius: 60px;
    height: 100%;
    width: 100%;
    object-fit: contain;
    margin: 1rem;
}

#price {
    color: rgba(64, 191, 255, 1);
}

form.formAddToCartt {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    align-items: center;
}

.quantity {
    display: inline-flex;
    flex-direction: row;
    align-content: center;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: center;
}

form.formAddToCartt pre {
    font-family: 'Readex Pro', sans-serif;
    font-size: 1.2rem;
}

form.formAddToCartt button {
    background: #ebf3fc;
    border: 1px solid #33A0FF;
    margin-left: 4rem;
    height: 2.8rem;
    width: 9rem;
}

form.formAddToCartt button strong {
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
}

.description-text {
    margin: 0rem 4rem;
}

</style>    
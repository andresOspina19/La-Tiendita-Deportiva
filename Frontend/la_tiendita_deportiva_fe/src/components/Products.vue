<template>    
        <div class="card-columns">
            <div class="card" v-for="product in productsByNameOrWithoutNameOrderedBySales.products" :key="product.productId"  @click="goToProductDetails(product.productId)">
                <img class="card-img-top" :src="product.imageURL">
                <div class="card-body">
                    <strong class="card-title">{{product.productName}}</strong>
                    <p class="price">{{this.convertToCOP(product.price)}} COP</p>                
                </div>
            </div>
        </div>

        <div class="container-pagination">
            <ul class="pagination">
            <li class="page-item" v-for="page in eachPage" :key="page">
                <router-link :to="{query: {search: this.$route.query.search, page: (page - 1)}}" class="page-link">{{page}}</router-link>
            </li>
        </ul>
        </div>
        
</template>

<script>
import gql from "graphql-tag";

export default {
    name: "Products",

    data: function() {
        return {
            productsByNameOrWithoutNameOrderedBySales: Object,            
        };
    },

    computed: {
        pages: {
            get: function () {
                return this.productsByNameOrWithoutNameOrderedBySales.totalPages;
            },
            set: function () {},
        },
        eachPage: {
            get: function () {
                return this.makePagination();
            },
            set: function () {},
        },
    },

    methods: {
        makePagination: function () {
            let eachPagee = [];

            for (let i = 1; i <= this.pages; i++) {
                eachPagee.push(i);
            }
            return eachPagee;
        },
        goToProductDetails: function (productId) {
            this.$router.push({ name: 'productDetail', query: { id: productId }});
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
        productsByNameOrWithoutNameOrderedBySales: {
            query: gql`
            query ($pagination: paginationProductsInput) {
                productsByNameOrWithoutNameOrderedBySales(pagination: $pagination) {
                    totalItems
                    totalPages
                    currentPage
                    products {
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
            }
          `,
            variables() {
                return {
                    pagination: {
                        productName: this.$route.query.search,
                        page: Number(this.$route.query.page),
                        size: 9
                    }
                };
            }      
        },
    },
}
</script>

<style>
.card-columns {
    display: grid;
    align-content: center;
    justify-content: center;
    align-items: end;
    justify-items: center;
    grid-template-columns: repeat(auto-fit, minmax(10rem, 20rem));
    gap: 5rem;
    margin: 3rem;
}

.card {
    min-width:300px;
    min-height:300px;
    border-radius: 50px;
    background: #ffffff;
    box-shadow:  20px 20px 60px #d9d9d9,
             -20px -20px 60px #ffffff;
}

.card-img-top {

    min-width:305px;
    min-height:305px;
    max-width:305px;
    max-height:305px;
    border-radius: 50px 50px 0px 0px;
}

.card-title{
    color: rgba(34, 50, 99, 1);
}

.price {
    color: rgba(64, 191, 255, 1);
}

.card-body {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.container-pagination {
    text-align:center;
}
 
ul.pagination {
    
    display: inline-flex;
    justify-content: center;
    align-items: center;
    flex-wrap: nowrap;
    align-content: center;
    flex-direction: row;
}

.page-item {
    list-style:none;
    width: 2rem;
    height: 2rem;
    margin: rem;
    display: inline-flex;
    justify-content: center;
    align-items: center;
    flex-wrap: nowrap;
    align-content: center;
    flex-direction: row;
    background: #EDEDED;
}

.page-item:hover {
    background: rgba(64, 191, 255, 1);
}

.page-link {
    text-decoration: none;
    font-size:1.5rem;
    color: rgba(34, 38, 42, 1);
}

.page-link:hover {
    text-decoration: none;
    font-size:1.5rem;
    color: rgba(255, 255, 255, 1);
}
</style>
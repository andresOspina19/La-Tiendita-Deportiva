<template>    
    <div class="card-columns">
        <div class="card" v-for="product in productsByNameOrWithoutNameOrderedBySales.products" :key="product.productId" style="width: 18rem;">
            <img class="card-img-top" :src="product.imageURL">
            <div class="card-body">
                <strong class="card-title">{{product.productName}}</strong>
                <p class="card-text">${{product.price}} COP</p>
                
            </div>
        </div>
    </div>


    <ul class="pagination pagination-sm">
        <li class="page-item disabled">
        <a class="page-link" href="#" tabindex="-1">1</a>
        </li>
        <li class="page-item"><a class="page-link" href="#">2</a></li>
        <li class="page-item"><a class="page-link" href="#">3</a></li>
    </ul>
    <h1>
        {{productsByNameOrWithoutNameOrderedBySales.products}}
    </h1>
</template>

<script>
import gql from "graphql-tag";

export default {
    name: "Products",

    data: function() {
        return {
            productsByNameOrWithoutNameOrderedBySales: [],
            pages: [],            
        };
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
    }
    
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
    border: 1px solid #000;
    min-width:300px;
    min-height:300px;
    border-radius: 50px;
    background: #ffffff;
    box-shadow:  20px 20px 60px #d9d9d9,
             -20px -20px 60px #ffffff;
}

.card-img-top {
    min-width:300px;
    min-height:300px;
    max-width:300px;
    max-height:300px;
    border-radius: 50px 50px 0px 0px;
}

.card-body {
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>
const { invertBy } = require("lodash");

const inventoryResolver = {
    Query: {
        productByproductId: async(_, { productId }, { dataSources, userIdToken }) => {
            if (productId == userIdToken)
                return dataSources.productAPI.productByProductId(productId)
            else
                return dataSources.productAPI.productByProductId(productId)
            },
        inventoryByProducId: async(_, { productId }, { dataSources, userIdToken }) => {
            if (productId == userIdToken)
                return dataSources.productAPI.inventoryByProductId(productId)
            else
                return dataSources.productAPI.inventoryByProductId(productId)
            }
    },
    Mutation: {
        createProduct: async(_, { product }, { dataSources, userIdToken }) => {
            const productinput = {
                productId: product.productId,
                productName: product.productName,
                imageURL: product.imageURL,
                price: product.price,
                stock: product.stock,
                description: product.description,
                category: product.category
            }
            return await dataSources.productAPI.createProduct(product);
        },
        createInventorySale: async(_, { inventory }, { dataSources, userIdToken }) => {
            const inventorysale = {
                id: inventory.id,
                productId: inventory.productId,
                stock: inventory.stock,
                movement: inventory.movement,
                modifyDate: inventory.modifyDate
            }
            return await dataSources.productAPI.createInventorySale(inventory);
        },
        createInventoryAdd: async(_, { inventory }, { dataSources, userIdToken }) => {
            const inventoryinput = {
                id: inventory.id,
                productId: inventory.productId,
                stock: inventory.stock,
                movement: inventory.movement,
                modifyDate: inventory.modifyDate
            }
            return await dataSources.productAPI.createInventoryAdd(inventory);
        }
        
    }
};

module.exports = inventoryResolver;
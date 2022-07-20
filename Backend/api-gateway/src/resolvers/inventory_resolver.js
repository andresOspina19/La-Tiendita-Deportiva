const { invertBy } = require("lodash");

const inventoryResolver = {
    Query: {
        productByproductId: async(_, { productId }, { dataSources, userIdToken }) => {
            if (productId == userIdToken)
                return dataSources.inventoryAPI.productByProductId(productId);
            else
                return dataSources.inventoryAPI.productByProductId(productId);
            },
        inventoryByProducId: async(_, { productId }, { dataSources, userIdToken }) => {
            if (productId == userIdToken)
                return dataSources.inventoryAPI.inventoryByProductId(productId);
            else
                return dataSources.inventoryAPI.inventoryByProductId(productId);
            },
        productsByNameOrWithoutNameOrderedBySales: async(_, { pagination }, { dataSources, userIdToken }) => {
            return dataSources.inventoryAPI.productsByNameOrWithoutNameOrderedBySales(pagination);
        }, 

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
            return await dataSources.inventoryAPI.createProduct(product);
        },
        createInventorySale: async(_, { inventory }, { dataSources, userIdToken }) => {
            const inventorysale = {
                id: inventory.id,
                productId: inventory.productId,
                stock: inventory.stock,
                movement: inventory.movement,
                modifyDate: inventory.modifyDate
            }
            return await dataSources.inventoryAPI.createInventorySale(inventory);
        },
        createInventoryAdd: async(_, { inventory }, { dataSources, userIdToken }) => {
            const inventoryinput = {
                id: inventory.id,
                productId: inventory.productId,
                stock: inventory.stock,
                movement: inventory.movement,
                modifyDate: inventory.modifyDate
            }
            return await dataSources.inventoryAPI.createInventoryAdd(inventory);
        }
        
    }
};

module.exports = inventoryResolver;
const { update } = require("lodash");

const cartResolver = {
    Query: {
        getUserCart: async(_, {username}, {dataSources, userIdToken}) => {
            console.log("voy a revisar el username");
            usernameToken = (await dataSources.authAPI.getUser(userIdToken)).username;
            if (username == usernameToken) 
                return await dataSources.inventoryAPI.getCartByUsername(username);
            else 
                return null;
        },
    },
    Mutation: {
        addCartItem: async(_, {addToCartInput}, {dataSources, userIdToken}) => {
            usernameToken = (await dataSources.authAPI.getUser(userIdToken)).username;
            if (addToCartInput.username == usernameToken) 
                return await dataSources.inventoryAPI.addProductToCart(addToCartInput);
            else 
                return null;
        },
        updateCartItem: async(_, {updateCartItem}, {dataSources, userIdToken}) => {
            usernameToken = (await dataSources.authAPI.getUser(userIdToken)).username;
            if (updateCartItem.username == usernameToken) 
                return await dataSources.inventoryAPI.UpdateProductOfCart(updateCartItem);
            else
                return null;
        },
        deleteCartItem: async(_, {deleteItemInput}, {dataSources, userIdToken}) => {
            usernameToken = (await dataSources.authAPI.getUser(userIdToken)).username;
            if (deleteItemInput.username == usernameToken) 
                return await dataSources.inventoryAPI.deleteProductOfCartByCartItemId(deleteItemInput);
            else
                return null;
        },
    }
};

module.exports = cartResolver;
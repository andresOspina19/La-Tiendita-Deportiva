const orderResolver = {
    Query: {
        getOrderById: async(_, {orderByIdInput}, {dataSources, userIdToken}) => {
            usernameToken = (await dataSources.authAPI.getUser(userIdToken)).username;
            if (orderByIdInput.username == usernameToken) { 
                let order = await dataSources.inventoryAPI.getOrderByOrderId(orderByIdInput);
                if (order.payed == false) {
                    let confirmOrDeleteOrder = {
                        username: order.username,
                        orderId: order.orderId
                    }
                    let paymentTokenUsername = {
                        token: order.paymentToken,
                        username: order.username
                    }
    
                    let status = (await dataSources.payAPI.getPaymentByToken(paymentTokenUsername)).status;
    
                    if (status == "approvedTransaction") {
                        console.log("pago aprobado! pedido aprobado resolver!")
                        await dataSources.inventoryAPI.ConfirmOrderIsPayed(confirmOrDeleteOrder);
                        return await dataSources.inventoryAPI.getOrderByOrderId(orderByIdInput);
                    } else if (status == "declinedTransaction") {
                        await dataSources.inventoryAPI.deleteOrderDeclined(confirmOrDeleteOrder);
                        return null;
                    }
                } else {
                    return order;
                }
            } else { 
                return null;
            }
        },
        getAllOrdersByUsername: async(_, {username}, {dataSources, userIdToken}) => {
            usernameToken = (await dataSources.authAPI.getUser(userIdToken)).username;
            if (username == usernameToken) {
                let orders = await dataSources.inventoryAPI.getAllOrdersByUsername(username);
                let ordersToReturn = [];
                for (let i = 0; i < orders.length; i++) {
                    let order = orders[i];
                    
                    if (order.payed == false) {
                        let confirmOrDeleteOrder = {
                            username: order.username,
                            orderId: order.orderId
                        }
                        let paymentTokenUsername = {
                            token: order.paymentToken,
                            username: order.username
                        }
    
                        let status = (await dataSources.payAPI.getPaymentByToken(paymentTokenUsername)).status;
    
                        if (status == "approvedTransaction") {
                            await dataSources.inventoryAPI.ConfirmOrderIsPayed(confirmOrDeleteOrder);
                            order = await dataSources.inventoryAPI.getOrderByOrderId(confirmOrDeleteOrder);
                            ordersToReturn.push(order);
                        } else if (status == "declinedTransaction") {
                            await dataSources.inventoryAPI.deleteOrderDeclined(confirmOrDeleteOrder);
                            delete order;
                        }
                    } else {
                        ordersToReturn.push(order);
                    }
                }
                return ordersToReturn;
            } else {
                return null;
            }
        },
    }
};

module.exports = orderResolver;
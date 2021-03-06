package gov.iti.jets.soapapi.managers;

import gov.iti.jets.domain.enums.Status;
import gov.iti.jets.domain.models.CartLineItem;
import gov.iti.jets.domain.models.Order;
import gov.iti.jets.domain.models.Product;
import gov.iti.jets.domain.models.User;
import gov.iti.jets.domain.services.OrderService;
import gov.iti.jets.domain.services.ProductService;
import gov.iti.jets.domain.services.UserService;
import gov.iti.jets.soapapi.custom.SoapCustomException;
import gov.iti.jets.soapapi.dtos.SoapLineItemDto;
import gov.iti.jets.soapapi.dtos.SoapOrderDto;
import gov.iti.jets.soapapi.dtos.SoapResponse;
import gov.iti.jets.soapapi.dtos.SoapUserDto;
import gov.iti.jets.soapapi.mappers.ResponseMapper;
import jakarta.jws.WebService;
import java.util.List;
import java.util.stream.Collectors;

@WebService
public class UserManager {

    public List<SoapUserDto> getAllUsers(){

        try {
            List<User> userList = UserService.getAllUsers();


            return userList.stream().map( SoapUserDto::new ).collect( Collectors.toList() );

        }catch(Exception e){
            e.printStackTrace();
            throw new SoapCustomException( "Couldn't retrieve users", e );
        }
    }

    public SoapUserDto getUserById(int userId){

        try {

            User user = UserService.getUserById( userId );
            return new SoapUserDto(user);

        }catch ( NullPointerException ne ){
            ne.printStackTrace();
            throw new SoapCustomException( "No user with this id", ne );
        }catch(Exception e){
            e.printStackTrace();
            throw new SoapCustomException( "Couldn't retrieve users", e );
        }
    }

    public SoapResponse addNewUser(SoapUserDto addRequestUser){

        try {

            User newUser = new User(addRequestUser.getFirstName(), addRequestUser.getLastName(), addRequestUser.getEmail(), addRequestUser.getRole());

            UserService.addNewUser( newUser );

            return new SoapResponse( "User added successfully" );

        }catch(Exception e){
            e.printStackTrace();
            throw new SoapCustomException( "User not added", e );
        }

    }




    public SoapResponse updateUser(int userId, SoapUserDto updatedRequestUser){
        try{

            User existingUser = UserService.getUserById( userId );

            existingUser.setFirstName( updatedRequestUser.getFirstName() );
            existingUser.setLastName( updatedRequestUser.getLastName() );
            existingUser.setEmail( updatedRequestUser.getEmail() );
            existingUser.setRole(updatedRequestUser.getRole());

            UserService.updateUser( existingUser );

            return new SoapResponse( "user updated successfully" );

        }catch ( NullPointerException ne ){
            ne.printStackTrace();
            throw new SoapCustomException( "No user with this id", ne );
        }catch(Exception e){
            e.printStackTrace();
            throw new SoapCustomException( "Couldn't update user", e );
        }
    }


    public SoapUserDto deleteUser(int userId){
        try{

            User user = UserService.getUserById( userId );

            UserService.deleteUser( user );

            return new SoapUserDto(user);

        }catch ( NullPointerException ne ){
            ne.printStackTrace();
            throw new SoapCustomException( "No user with this id", ne );
        }catch(Exception e){
            e.printStackTrace();
            throw new SoapCustomException( "Couldn't update user", e );
        }
    }



    public SoapResponse addItemToUserCart( int userId, SoapLineItemDto addedRequestItem ){
        try{

            Product lineItemProduct = ProductService.getProductById( addedRequestItem.getProductId() );

            CartLineItem addedItem = new CartLineItem();
            addedItem.setProduct( lineItemProduct );
            addedItem.setQuantity( addedRequestItem.getQuantity() );

            UserService.addItemToCart(userId, addedItem);

            return new SoapResponse( "Item added successfully" );

        }catch ( Exception e ){
            e.printStackTrace();
            throw new SoapCustomException( "Couldn't add item", e );
        }
    }

    public List<SoapLineItemDto> getUserCart( int userId){
        try{

            User user = UserService.getUserById( userId );

            return user.getCart().getLineItems().stream().map( SoapLineItemDto::new ).collect( Collectors.toList());

        }catch ( NullPointerException ne ){
            ne.printStackTrace();
            throw new SoapCustomException( "No user with this id", ne );
        }catch(Exception e){
            e.printStackTrace();
            throw new SoapCustomException( "Couldn't retieve user's cart", e );
        }
    }

    public SoapResponse removeItemFromUserCart(int userId, int itemId){
        try{

            UserService.removeItemFromCart(userId, itemId);

            return new SoapResponse( "Item removed successfully" );

        }catch ( Exception e ){
            e.printStackTrace();
            throw new SoapCustomException( "Couldn't remove item from user's cart", e );
        }

    }


    public SoapResponse emptyUserCart(int userId){
        try{

            UserService.emptyCart(userId);

            return new SoapResponse( "User's cart emptied successfully" );

        }catch ( Exception e ){
            e.printStackTrace();
            throw new SoapCustomException( "Couldn't empty user's cart", e );
        }
    }




    public SoapResponse placeOrder(int userId){
        try{

            User user = UserService.getUserById( userId );
            UserService.placeOrder(user);
            return new SoapResponse("Order Placed successfully");

        } catch ( NullPointerException ne ){
            ne.printStackTrace();
            throw new SoapCustomException( "No user with this id", ne );
        }catch(Exception e){
            e.printStackTrace();
            throw new SoapCustomException( "Order not placed", e );
        }

    }

    public List<SoapOrderDto> getUserOrders( int userId){
        try{

            User user = UserService.getUserById( userId );

            List<SoapOrderDto> orderDtoList = user.getOrders().stream().map( ResponseMapper::mapOrderToSoapOrderDto ).collect( Collectors.toList());

            System.out.println(user.getOrders());

            return orderDtoList;

        } catch ( NullPointerException ne ){
            ne.printStackTrace();
            throw new SoapCustomException( "No user with this id", ne );
        }catch(Exception e){
            e.printStackTrace();
            throw new SoapCustomException( "Couldn't retrieve orders", e );
        }
    }



    public SoapOrderDto getOrderById(int orderId) {

        try{
            Order retrievedOrder = OrderService.getOrderById(orderId);

            SoapOrderDto responseOrder = ResponseMapper.mapOrderToSoapOrderDto( retrievedOrder );

            return responseOrder;

        }catch(NullPointerException ne){
            throw new SoapCustomException("No order with this id" , ne);
        }catch ( Exception e ){
            throw new SoapCustomException( "Couldn't retrieve order", e );
        }

    }

    public SoapResponse changeOrderStatus( int orderId, Status newStatus ){

        try{

            Order retrievedOrder = OrderService.getOrderById(orderId);

            retrievedOrder.setStatus( newStatus );

            OrderService.updateOrder(retrievedOrder);

            return new SoapResponse( "Order status updated successfully" );

        }catch(NullPointerException ne){
            throw new SoapCustomException( "No order with this id" , ne);
        }catch ( Exception e ){
            throw new SoapCustomException("Couldn't update status", e );
        }
    }



/*    public static void main( String[] args ) {
        UserManager test = new UserManager();
        System.out.println( "\n all users " + test.getAllUsers());
        System.out.println("\n user By Id" +test.getUserById( 1 ));
        System.out.println("\n user cart " +test.getUserCart( 1 ));
        System.out.println("\n add item to cart " + test.addItemToUserCart( 1, new SoapLineItemDto(1,20) ));
        System.out.println("\n user cart "  + test.getUserCart( 1 ));
        System.out.println("\n place order " + test.placeOrder( 1 ));
        System.out.println("\n  get user oders " + test.getUserOrders( 1 ));

        System.out.println("\n order by id0  "+ test.getOrderById( 2 ));
        System.out.println("\n update order status  "+ test.changeOrderStatus( 2, Status.APPROVED ));
        System.out.println("\n order by id0  "+ test.getOrderById( 2 ));


    }*/

}

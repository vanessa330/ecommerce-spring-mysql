package com.ecom.cms.Purchase;

import com.ecom.cms.JWT.JwtFilter;
import com.ecom.cms.Purchase.Item.Item;
import com.ecom.cms.Purchase.Item.ItemRepository;
import com.ecom.cms.Product.Product;
import com.ecom.cms.Product.ProductRepository;
import com.ecom.cms.UTILS.MainConstants;
import com.ecom.cms.UTILS.MainUtils;
import com.ecom.cms.User.User;
import com.ecom.cms.User.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PurchaseController implements PurchaseService {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addPurchase(Map<String, Object> requestMap) {
        try {
            Purchase newPurchase = buildPurchaseFromMap(requestMap);
            purchaseRepository.save(newPurchase);

            return MainUtils.getResponseEntity("Purchase added successfully", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //        JSON requestMap
//    {
//        "items": [{
//            "productId": 1, "quantity": 2, "color": "red"
//        },
//        {
//            "productId": 2, "quantity": 3, "color": ""
//        }],
//         "userId": 1
//    }
    private Purchase buildPurchaseFromMap(Map<String, Object> requestMap) {

//        1. Build the Item models.
        BigDecimal subTotal = BigDecimal.valueOf(0.00);

        List<Item> items = new ArrayList<>();
        for (Map<String, Object> itemMap : (List<Map<String, Object>>) requestMap.get("items")) {

            Integer productId = ((Number) itemMap.get("productId")).intValue();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + productId));

            int quantity = ((Number) itemMap.get("quantity")).intValue();

            String productColor = (String) itemMap.get("color");

            Item newItem = Item.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .quantity(quantity)
                    .productColor(productColor)
                    .pricePerItem(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                    .build();

            subTotal = subTotal.add(newItem.getPricePerItem());
            items.add(newItem);
            itemRepository.save(newItem);
        }

//        2. Build the Purchase model.
        BigDecimal shippingCost = BigDecimal.valueOf(30.00); // set default to $30

        Purchase newPurchase = Purchase.builder()
                .status("pending")
                .subTotal(subTotal)
                .shippingCost(shippingCost)
                .totalPrice(subTotal.add(shippingCost))
                .createdDate(LocalDateTime.now())
                .build();

//        3. In the Purchase model, set the user
        int userId;
        if (requestMap.containsKey("userId") && requestMap.get("userID") != null) {
            userId = ((Number) requestMap.get("userId")).intValue();
        } else {
            userId = 1; // Set default value to 1 for the guest
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + userId));
        newPurchase.setUser(user);

//        4. Update each item purchase_id
        for (Item item : items) {
            item.setPurchase(newPurchase);
        }

//        5. return to the addPurchase()
        return newPurchase;
    }

    @Override
    public List<PurchaseDto> getAllPurchases() {
        List<Purchase> purchaseList = purchaseRepository.findAll();
        List<PurchaseDto> purchaseDtoList = purchaseList.stream()
                .map(purchase -> modelMapper.map(purchase, PurchaseDto.class))
                .collect(Collectors.toList());
        return purchaseDtoList;
    }

    @Override
    public List<PurchaseDto> getByUser(int userId) {
        List<Purchase> purchaseList = purchaseRepository.findByUser(userId);
        List<PurchaseDto> purchaseDtoList = purchaseList.stream()
                .map(purchase -> modelMapper.map(purchase, PurchaseDto.class))
                .collect(Collectors.toList());
        return purchaseDtoList;
    }

    //    JSON requestMap
//    {
//        "id": 2,
//        "status": "shipped"
//    }
    @Override
    public ResponseEntity<String> updateStatus(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            Optional<Purchase> optional = purchaseRepository.findById(((Number) requestMap.get("id")).intValue());

            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Purchase id doesn't exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            purchaseRepository.updateStatus(requestMap.get("status").toString(), ((Number) requestMap.get("id")).intValue());

            return MainUtils.getResponseEntity("Status updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

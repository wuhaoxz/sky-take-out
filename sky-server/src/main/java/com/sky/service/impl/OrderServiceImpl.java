package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {

        Long userId = BaseContext.getCurrentId();

        //1.插入订单表
        Orders order = new Orders();
        //根据地址id查询地址信息
        String uuid = UUID.randomUUID().toString();
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        order.setNumber(format+"-"+uuid);//订单号
        order.setStatus(Orders.PENDING_PAYMENT); //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款
        order.setUserId(userId);
        order.setAddressBookId(ordersSubmitDTO.getAddressBookId());
        order.setOrderTime(LocalDateTime.now());//下单时间
        order.setCheckoutTime(null);//结账时间
        order.setPayMethod(1);
        order.setPayStatus(Orders.UN_PAID);//支付状态 0未支付 1已支付 2退款
        order.setAmount(ordersSubmitDTO.getAmount());//todo 不可以用前端传来的总金额，必须后端自己计算总金额
        order.setRemark(ordersSubmitDTO.getRemark());
        //查询地址
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail());
        //查询用户信息
        User user = userMapper.getId(userId);
        order.setUserName(user.getName());//下单人
        order.setConsignee(addressBook.getConsignee());//收货人
        order.setEstimatedDeliveryTime(ordersSubmitDTO.getEstimatedDeliveryTime());
        order.setDeliveryStatus(ordersSubmitDTO.getDeliveryStatus());//配送状态  1立即送出  0选择具体时间
        order.setDeliveryTime(null);//送达时间
        order.setPackAmount(ordersSubmitDTO.getPackAmount());//todo 不可以用前端传来的金额，必须后端自己计算总金额
        order.setTablewareNumber(ordersSubmitDTO.getTablewareNumber());
        order.setTablewareStatus(ordersSubmitDTO.getTablewareStatus());
        //插入orders表
        orderMapper.save(order);




        //2.插入订单明细表
        List<OrderDetail> listOrderDetail = new ArrayList<>();
        //查询购物车表
        List<ShoppingCart> listCart = shoppingCartMapper.list(userId);
        listCart.forEach(cart->{
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(cart,detail);
            detail.setId(null);
            detail.setOrderId(order.getId());
            listOrderDetail.add(detail);
        });

        orderDetailMapper.saveBatch(listOrderDetail);


        //3.删除购物车
        shoppingCartMapper.cleanAll(userId);

        //4.构建OrderSubmitVO对象并返回

        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();

        orderSubmitVO.setId(order.getId());//订单id
        orderSubmitVO.setOrderNumber(order.getNumber());//订单号
        orderSubmitVO.setOrderAmount(order.getAmount());//订单金额
        orderSubmitVO.setOrderTime(order.getOrderTime());//下单时间



        return orderSubmitVO;
    }

    @Override
    public void payment(OrdersPaymentDTO ordersPaymentDTO) {

        orderMapper.payment(ordersPaymentDTO);

    }

    @Override
    public PageResult page(OrdersPageQueryDTO ordersPageQueryDTO) {

        //查询orders表
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Orders orders = new Orders();
        orders.setUserId(BaseContext.getCurrentId());//用户id
        orders.setStatus(ordersPageQueryDTO.getStatus());//订单状态
        Page<Orders> ordersList = orderMapper.page(orders);//指定用户的订单list



        List<OrderVO> orderVOS = new ArrayList<>();


        //ordersList
        for (Orders ord : ordersList) {
            OrderVO orderVO = new OrderVO();

            //拷贝ord中的属性
            BeanUtils.copyProperties(ord,orderVO);

            //获得指定订单的订单明细list
            Long orderId = ord.getId();
            List<OrderDetail> details = orderDetailMapper.getByOrderId(orderId);
            orderVO.setOrderDetailList(details);

            //根据details得到所有菜品信息组装成字符串
            List<String> collect = details.stream().map(new Function<OrderDetail, String>() {
                @Override
                public String apply(OrderDetail orderDetail) {
                    if(orderDetail.getDishFlavor()!=null){
                        return orderDetail.getName()+"(" + orderDetail.getDishFlavor() +")*"+ orderDetail.getNumber()+"; ";
                    }
                    return orderDetail.getName()+"*"+ orderDetail.getNumber()+"; ";
                }
            }).collect(Collectors.toList());
            String orderDishesStr = String.join("", collect);
            orderVO.setOrderDishes(orderDishesStr);

            orderVOS.add(orderVO);
        }

        //封装返回对象
        PageResult pageResult = new PageResult();
        pageResult.setTotal(ordersList.getTotal());
        pageResult.setRecords(orderVOS);

        return pageResult;
    }

    @Override
    public OrderVO getOrderById(Long id) {//id:订单id

        OrderVO orderVO = new OrderVO();
        Orders order = orderMapper.getOrderById(id);

        BeanUtils.copyProperties(order,orderVO);


        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        orderVO.setOrderDetailList(orderDetailList);


        return orderVO;
    }

    @Override
    public void repetition(Long id) {
        //从订单明细表中查询数据，并放入到购物车中，即可
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        List<ShoppingCart> shoppingCartList = new ArrayList<>();

        for (OrderDetail detail : orderDetailList) {
            ShoppingCart cart = new ShoppingCart();
            BeanUtils.copyProperties(detail,cart);
            cart.setUserId(BaseContext.getCurrentId());
            cart.setCreateTime(LocalDateTime.now());

            shoppingCartList.add(cart);
        }


        shoppingCartMapper.saveBatch(shoppingCartList);


    }



    @Override
    public void cancel(Long id) {


        //1.查询订单表
        Orders order = orderMapper.getOrderById(id);
        if(order.getPayStatus()==1){
            //如果是已支付，则status=7，且payStatus=2
            // order.setStatus(7);
            order.setStatus(6);//订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
            order.setPayStatus(2);//支付状态 0未支付 1已支付 2退款
        }else if(order.getPayStatus()==0){
            //如果是未支付，则status=6，且payStatus=0
            order.setStatus(6);
            order.setPayStatus(0);
        }
        order.setCancelReason("用户主动取消");
        order.setCancelTime(LocalDateTime.now());

        orderMapper.cancel(order);
    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {

        //查询orders表
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> ordersList = orderMapper.conditionSearch(ordersPageQueryDTO);//指定用户的订单list



        List<OrderVO> orderVOS = new ArrayList<>();


        //ordersList
        for (Orders ord : ordersList) {
            OrderVO orderVO = new OrderVO();

            //拷贝ord中的属性
            BeanUtils.copyProperties(ord,orderVO);

            //获得指定订单的订单明细list
            Long orderId = ord.getId();
            List<OrderDetail> details = orderDetailMapper.getByOrderId(orderId);
            orderVO.setOrderDetailList(details);

            //根据details得到所有菜品信息组装成字符串
            List<String> collect = details.stream().map(new Function<OrderDetail, String>() {
                @Override
                public String apply(OrderDetail orderDetail) {
                    if(orderDetail.getDishFlavor()!=null){
                        return orderDetail.getName()+"(" + orderDetail.getDishFlavor() +")*"+ orderDetail.getNumber()+"; ";
                    }
                    return orderDetail.getName()+"*"+ orderDetail.getNumber()+"; ";
                }
            }).collect(Collectors.toList());
            String orderDishesStr = String.join("", collect);
            orderVO.setOrderDishes(orderDishesStr);

            orderVOS.add(orderVO);
        }

        //封装返回对象
        PageResult pageResult = new PageResult();
        pageResult.setTotal(ordersList.getTotal());
        pageResult.setRecords(orderVOS);

        return pageResult;
    }

    @Override
    public OrderStatisticsVO statistics() {

        OrderStatisticsVO vo = new OrderStatisticsVO();
        Integer ToBeConfirmed = orderMapper.countByStatus(2);//订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        Integer Confirmed = orderMapper.countByStatus(3);
        Integer DeliveryInProgress = orderMapper.countByStatus(4);


        vo.setConfirmed(Confirmed);
        vo.setToBeConfirmed(ToBeConfirmed);
        vo.setDeliveryInProgress(DeliveryInProgress);



        return vo;
    }

    @Override
    public void confirm(Long id) {
        orderMapper.confirm(id);
    }

    @Override
    public void rejection(Orders orders) {
        orderMapper.rejection(orders);
    }

    @Override
    public void delivery(Long id) {
        orderMapper.delivery(id);
    }

    @Override
    public void complete(Long id) {
        orderMapper.complete(id);
    }



    @Override
    public void cancelByAdmin(Orders orders) {


        //1.查询订单表
        Orders order = orderMapper.getOrderById(orders.getId());
        if(order.getPayStatus()==1){
            //如果是已支付，则status=7，且payStatus=2
            // order.setStatus(7);
            order.setStatus(6);//订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
            order.setPayStatus(2);//支付状态 0未支付 1已支付 2退款
        }else if(order.getPayStatus()==0){
            //如果是未支付，则status=6，且payStatus=0
            order.setStatus(6);
            order.setPayStatus(0);
        }
        order.setCancelTime(LocalDateTime.now());

        order.setCancelReason("商家取消："+orders.getCancelReason());

        orderMapper.cancel(order);
    }


}

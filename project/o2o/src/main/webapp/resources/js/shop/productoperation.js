$(function(){
	//从URL里获取productId参数的值
	var productId=getQueryString('productId');
	//通过productId获取商品信息的URL
	var infoUrl='/o2o/shopadmin/getproductbyid?productId='+productId;
	//获取当前店铺设定的商品类别列表的URL
	var categoryUrl='/o2o/shopadmin/getproductcategorylist';
	//更新商品信息的URL
	var productPostUrl='/o2o/shopadmin/modifyproduct';
	//由于商品添加和编辑使用的是同一个页面,该标识符用来标明本次是添加还是编辑操作
	var isEdit=false;
	if(productId){
		//如果有productId则为编辑操作
		getInfo(productId);//不为空，则先获取商品原来的信息
		isEdit=true;
	}else{
		getCategory();//如果是要新增的话，则需要先新增商品的Category类别
		productPostUrl='/o2o/shopadmin/addproduct';
	}
	//获取需要编辑的商品的商品信息，并赋值给表单
	function getInfo(id){
		$
			.getJSON(
					infoUrl,
					function(data){
						if(data.success){
							//从返回的ISON当中获取product对象的信息，并赋值给表单
							var product=data.product;
							$('#product-name').val(product.productName);
							$('#product-desc').val(product.productDesc);
							$('#priority').val(product.priority);
							$('#normal-price').val(product.normalPrice);
							$('#promotion-price').val(
									product.promotionPrice);
							//获取原本的商品类别以及该店铺的所有商品类别列表
							var optionHtml='';
							var optionArr=data.productCategoryList;
							var optionSelected=product.productCategory.productCategoryId;
							//生成前端的HTML商品类别列表，并默认选择编辑前商品类别
							optionArr
									.map(function(item, index){
										var isSelect=optionSelected==item.productCategoryId ? 'selected'
												:'';
										optionHtml+='<option data-value="'
												+item.productCategoryId
												+'"'
												+isSelect
												+'>'
												+item.productCategoryName
												+'</option>';
									});
							$('#category').html(optionHtml);
						}
					});
	}
	//为商品添加操作提供该店铺下的所有商品类别列表,这个提供的类别列表之前已经过了，这个category访问的是ProductCategoryManagementController下面的
	//"/getproductcategorlist"
	function getCategory(){
		$.getJSON(categoryUrl,function(data){
			if(data.success){
				var productCategoryList=data.data;
				var optionHtml='';
				productCategoryList.map(function(item, index){//将返回的结果添加到前台
					optionHtml+='<option data-value="'
							+item.productCategoryId+'">'
							+item.productCategoryName+'</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}
	
	//针对商品详情图控件组，若该控件组的最后一个元素发生变化(即上传了图片),且控件总数未达到6个，则生成一个文件上传控件
	$('.detail-img-div').on('change','.detail-img:last-child',function(){
		if($('.detail-img').length<6){
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});
	
	//按提交按钮的事件响应，分别对商品添加和编辑操作做不同的响应
	$('#submit').click(
			function(){
				//它们的相同之处：1.创建JSON对象，并从表单里面获取对应的属性值
				var product={};
				product.productName=$('#product-name').val();
				product.productDesc=$('#product-desc').val();
				product.priority=$('#priority').val();
				product.normalPrice=$('#normal-price').val();
				product.promotionPrice=$('#promotion-price').val();
				//2.获取对应的商品类别值
				product.productCategory={
						productCategoryId:$('#category').find('option').not(
								function(){
									return !this.selected;
								}).data('value')
				};
				product.productId=productId;
				//3.获取缩略图文件流
				var thumbnail=$('#small-img')[0].files[0];
				//4.生成表单对象，用于接收参数并传递给后台
				var formData=new FormData();
				formData.append('thumbnail',thumbnail);
				//5.遍历商品详情图控件，获取里面的文件流
				$('.detail-img').map(
						function(index, item) {
							// 判断该控件是否已选择了文件，每次点击最后一个都会生成一个新的
							if ($('.detail-img')[index].files.length > 0) {
								// 将第i个文件流赋值给key为productImgi的表单键值对里
								formData.append('productImg' + index,
										$('.detail-img')[index].files[0]);
							}
						});
				// 将product json对象转成字符流保存至表单对象key为productStr的的键值对里
				formData.append('productStr', JSON.stringify(product));
				// 获取表单里输入的验证码
				var verifyCodeActual = $('#j_captcha').val();
				if (!verifyCodeActual) {
					$.toast('请输入验证码!');
					return;
				}
				formData.append("verifyCodeActual", verifyCodeActual);
				// 将数据提交至后台处理相关操作
				$.ajax({
					url : productPostUrl,
					type : 'POST',
					data : formData,
					contentType : false,
					processData : false,
					cache : false,
					success : function(data) {
						if (data.success) {
							$.toast('提交成功!');
							$('#captcha_img').click();//提交成功或失败都换一个验证码
						} else {
							$.toast('提交失败! ');
							$('#captcha_img').click();
						}
					}
				});
			});

});
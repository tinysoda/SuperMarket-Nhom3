Table User {
  id int [pk, increment]
  name varchar(255)
  phone varchar(20)
  email varchar(255)
  password varchar(255)
  role ENUM('manager','employee')

}

Table Product {
  id int [pk, increment] // Mã sản phẩm
  name varchar(255) // Tên sản phẩm
  description text // Mô tả sản phẩm
  price decimal(10, 2) // Giá sản phẩm
  quantity int // Số lượng tồn kho
  category_id int // Mã danh mục sản phẩm
  is_deleted int [default: 0] // Trạng thái xóa mềm (0: tồn tại, 1: không tồn tại) // Ngày tạo
 // Ngày cập nhật
}

Table Category {
  id int [pk, increment]
  name varchar(255)
  description text

}

Table Customer {
  id int [pk, increment]
  name varchar(255)
  phone varchar(20)
  email varchar(255)
  address varchar(255)
  points int

}

Table Bill {
  id int [pk, increment]
  customer_id int
  user_id int
  total_amount decimal(10, 2)
  created_at timestamp

}

Table BillDetail {
  id int [pk, increment]
  product_id int
  bill_id int
  quantity int
  price decimal(10, 2)

}

// Relationships
Ref: Product.category_id > Category.id
Ref: Bill.customer_id > Customer.id
Ref: Bill.user_id > User.id
Ref: BillDetail.bill_id > Bill.id
Ref: BillDetail.product_id > Product.id
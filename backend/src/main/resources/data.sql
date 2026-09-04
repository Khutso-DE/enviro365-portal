INSERT INTO investors (id, first_name, last_name, email, contact_number, age) VALUES
(1, 'Khutso', 'Nkadimeng', 'khutso@email.com', '0711111111', 68),
(2, 'David', 'Lesaomako', 'david@email.com', '0722222222', 72),
(3, 'Relebohile', 'Mofokeng', 'relebohile@email.com', '0733333333', 61),
(4, 'Mpho', 'Makola', 'mpho@email.com', '0744444444', 58);

INSERT INTO products (id, type, name, current_balance, investor_id) VALUES
(1, 'RETIREMENT', 'Retirement Growth', 2500.00, 1),
(2, 'SAVINGS', 'Emergency Fund', 800.00, 1),
(3, 'RETIREMENT', 'Pension Reserve', 1800.00, 2),
(4, 'SAVINGS', 'Travel Savings', 600.00, 2),
(5, 'SAVINGS', 'Education Savings', 1200.00, 3),
(6, 'RETIREMENT', 'Long Term Growth', 2200.00, 4);

INSERT INTO withdrawal_notices (id, withdrawal_amount, notice_date, status, banking_details, product_id) VALUES
(1, 150.00, CURRENT_TIMESTAMP, 'PENDING', 'FNB 123456789', 2);
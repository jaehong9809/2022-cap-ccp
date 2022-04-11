import cv2
import numpy as np
import pymysql

db = pymysql.connect(
    user='root',
    passwd='1234',
    host='localhost',
    db='food',
    charset='utf8'
)
cursor = db.cursor()
lst = [1, 4]
sum = 0
sql = "select calorie from foods where id = %s"
cursor.execute(sql, 1)
result = cursor.fetchall()

print(result)


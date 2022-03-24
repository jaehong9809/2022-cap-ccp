import cv2
import base64

image = cv2.imread("bb.jpg")
img_str = base64.b64encode(cv2.imencode('.jpg', image)[1]).decode()

print(type(img_str))
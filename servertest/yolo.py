import pymysql
import cv2
import numpy as np
import base64
import os
from PIL import ImageFont, ImageDraw, Image


# YOLO 가중치 파일과 CFG 파일 로드
net = cv2.dnn.readNet("yolov3_last.weights","yolov3.cfg")
# YOLO NETWORK 재구성
classes = []
with open("obj.names", "r") as f:
    classes = [line.strip() for line in f.readlines()]
layer_names = net.getLayerNames()
output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]
colors = np.random.uniform(0, 255, size=(len(classes), 3))


def process(image):
    db = pymysql.connect(
        user='root',
        passwd='1234',
        host='localhost',
        db='food',
        charset='utf8'
    )
    cursor = db.cursor()

    img = cv2.imread(image)
    #img = cv2.resize(img, None, fx=0.4, fy=0.4)
    height, width, channels = img.shape
    blob = cv2.dnn.blobFromImage(img, 0.00392, (832, 832), (0, 0, 0), True, crop=False)
    net.setInput(blob)
    outs = net.forward(output_layers)
    class_ids = []
    confidences = []
    boxes = []
    for out in outs:
        for detection in out:
            scores = detection[5:]
            class_id = np.argmax(scores)
            confidence = scores[class_id]
            if confidence > 0.2:
                # Object detected
                center_x = int(detection[0] * width)
                center_y = int(detection[1] * height)
                w = int(detection[2] * width)
                h = int(detection[3] * height)
                # 좌표
                x = int(center_x - w / 2)
                y = int(center_y - h / 2)
                boxes.append([x, y, w, h])
                confidences.append(float(confidence))
                class_ids.append(class_id)
    foods = []
    indexes = cv2.dnn.NMSBoxes(boxes, confidences, 0.5, 0.4)
    sql = "select name from foods where id = %s"
    food_names = []
    font = ImageFont.truetype("fonts/gulim.ttc", 50)

    for i in range(len(boxes)):
        if i in indexes:
            x, y, w, h = boxes[i]
            cursor.execute(sql, class_ids[i])
            result = cursor.fetchall()
            color = colors[i]
            cv2.rectangle(img, (x, y), (x + w, y + h), color, 2)
            img_pil = Image.fromarray(img)
            draw = ImageDraw.Draw(img_pil)
            draw.text((x, y+30), result[0][0], font=font, fill=(int(color[0]), int(color[1]), int(color[2])))
            img = np.array(img_pil)
            foods.append(class_ids[i])


    for i in range(len(foods)):
        cursor.execute(sql, foods[i])
        result = cursor.fetchall()
        food_names.append(result[0][0])


    img_str = base64.b64encode(cv2.imencode('.jpg', img)[1]).decode()

    db.commit()
    db.close()

    if os.path.isfile(image):
        os.remove(image)

    return food_names, img_str, len(food_names)

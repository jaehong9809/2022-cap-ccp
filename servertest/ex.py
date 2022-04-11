import numpy as np
from PIL import ImageFont, ImageDraw, Image
import cv2

img = cv2.imread("a.jpg")
font = ImageFont.truetype("fonts/gulim.ttc", 50)
img_pil = Image.fromarray(img)
draw = ImageDraw.Draw(img_pil)
draw.text((60, 70), "sex", font=font, fill=(0, 0, 0))

img = np.array(img_pil)
cv2.imshow("r", img)
cv2.waitKey()
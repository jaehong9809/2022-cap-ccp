from django.contrib.auth import authenticate
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import MyUser
from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
import yolo
import pymysql
import os
import datamod


@csrf_exempt
def join(request):
    if request.method == 'POST':
        username = request.POST.get('username', '')
        password = request.POST.get('password', '')
        age = request.POST.get('age', 0)
        sex = request.POST.get('sex', '')
        height = request.POST.get('height', 0)
        weight = request.POST.get('weight', 0)
        user = MyUser.objects.create_superuser(username, password, age, sex, height, weight)
        user.save()
        return JsonResponse({'code': '0000', 'msg': '회원가입 성공입니다.'}, status=200)


@csrf_exempt
def login(request):
    if request.method == 'POST':
        id = request.POST.get('userid', '')
        pw = request.POST.get('userpw', '')

        result = authenticate(username=id, password=pw)

        if result:
            print("성공")

            db = pymysql.connect(
                user='root',
                passwd='1234',
                host='localhost',
                db='food',
                charset='utf8'
            )
            cursor = db.cursor()
            sql = "select sex, weight, height, age from user where username = %s"
            cursor.execute(sql, id)
            result = cursor.fetchall()

            return JsonResponse({'code': '0000', 'sex': result[0][0], 'weight': result[0][1], 'height': result[0][2],
                                 'age': result[0][3]}, status=200)
        else:
            print("실패")
            return JsonResponse({'code': '1001', 'msg': '로그인실패입니다.'}, status=200)


@csrf_exempt
def calculate(request):
    if request.method == 'POST':
        file = request.FILES['proFile']
        default_storage.save(str(file), ContentFile(file.read()))
        date = request.POST.get('date', '')
        user = request.POST.get('id', '')
        sex = request.POST.get('sex', '')
        weight = int(request.POST.get('weight', ''))
        height = int(request.POST.get('height', ''))
        age = int(request.POST.get('age', ''))
        sum_calorie, img = yolo.process(str(file), user, date, sex, weight, height, age)

        return JsonResponse({'code': '0000', 'msg': str(sum_calorie), 'img': img},  status=200)


@csrf_exempt
def food(request):
    if request.method == 'POST':
        db = pymysql.connect(
            user='root',
            passwd='1234',
            host='localhost',
            db='food',
            charset='utf8'
        )
        cursor = db.cursor()

        time = request.POST.get('time', '')
        userid = request.POST.get('id', '')
        sql = "select food_name, tim, calorie, carbo, protein, fat from user_food where user = %s and tim like %s"
        cursor.execute(sql, (userid, "%"+time))
        result = cursor.fetchall()

        if len(result) == 0:
            return JsonResponse({"code": "0001"})
        else:
            lst = [list(foods) for foods in result]
            return JsonResponse({"foods": [list(foods) for foods in result], "code": "0000"}, status=200)


@csrf_exempt
def datainfo(request):
    if request.method == 'POST':
        datamod.agecalavglist
        datamod.monthavgcal

        return JsonResponse({'code': '0000', 'agecalavglist':datamod.agecalavglist, 'monthavgcal':datamod.monthavgcal}, status=200)

from django.contrib.auth import authenticate
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import MyUser
from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
import yolo
import pymysql
import datamod
import datetime


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

            db.commit()
            db.close()

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

        db.commit()
        db.close()

        if len(result) == 0:
            return JsonResponse({"code": "0001"})
        else:
            return JsonResponse({"foods": [list(foods) for foods in result], "code": "0000"}, status=200)


@csrf_exempt
def datainfo(request):
    if request.method == 'POST':
        datamod.agecalavglist
        datamod.monthavgcal

        return JsonResponse({'code': '0000', 'agecalavglist':datamod.agecalavglist, 'monthavgcal':datamod.monthavgcal}, status=200)


@csrf_exempt
def datainfo2(request):
    if request.method == 'POST':
        sex = request.POST.get('sex', '')
        height = int(request.POST.get('height'))
        weight = int(request.POST.get('weight'))
        age = int(request.POST.get('age'))


        if sex == "  남":
            sex = 'M'
        elif sex == "  여":
            sex = 'F'

        db = pymysql.connect(
            user='root',
            passwd='1234',
            host='localhost',
            db='food',
            charset='utf8'
        )
        cursor = db.cursor()
        sql = "select food_name from user_food where sex = %s and height between %s and %s and " \
              "weight between %s and %s and age between %s and %s"
        cursor.execute(sql, (sex, height, height+9, weight, weight+9, age, age+9))
        result = cursor.fetchall()

        foodcnt = {}
        for i in result:
            if i[0] in foodcnt:
                foodcnt[i[0]] += 1
            else:
                foodcnt[i[0]] = 1

        tmp1 = list(foodcnt)
        tmp2 = list(foodcnt.values())
        foodcntlist = []  # 리스트로 만들기
        for i in range(len(tmp1)):
            foodcntlist.append([tmp1[i], tmp2[i]])
        foodcntlist.sort(key=lambda x: -x[1])
        sendfood = []

        if len(foodcntlist) > 5:
            for i in range(0, 5):
                sendfood.append([foodcntlist[i][0], foodcntlist[i][1]])
        else:
            sendfood = foodcntlist
        print(foodcntlist)
        print(foodcnt)
        print(sex, height, weight, age)

        db.commit()
        db.close()

        return JsonResponse({'code': '0000', 'info': sendfood}, status=200)


@csrf_exempt
def userinfo(request):
    if request.method == 'POST':
        id = request.POST.get('id', '')

        db = pymysql.connect(
            user='root',
            passwd='1234',
            host='localhost',
            db='food',
            charset='utf8'
        )
        cursor = db.cursor()
        sql = "select tim, calorie from user_food where user = %s"
        cursor.execute(sql, id)
        result = cursor.fetchall()

        userdaycal15 = {}  # 날짜/ 칼로리
        userdaycal30 = {}
        now = datetime.datetime.now()

        for i in range(1, 16):
            tmp = now - datetime.timedelta(days=i)
            da = tmp.strftime(("%Y%m%d"))
            userdaycal15[da] = 0

        for i in range(1, 31):
            tmp = now - datetime.timedelta(days=i)
            da = tmp.strftime(("%Y%m%d"))
            userdaycal30[da] = 0

        for i in result:
            a = str(i[0])
            date = a[:8]
            if (date in userdaycal15):
                userdaycal15[date] += i[1]
        tmp1 = list(userdaycal15)
        tmp2 = list(userdaycal15.values())
        userdaycallist15 = []  # 리스트로 만들기
        for i in range(len(tmp1)):
            userdaycallist15.append([tmp1[i], tmp2[i]])
        print(userdaycallist15)
        userdaycallist30 = []
        for i in result:
            a = str(i[0])
            date = a[:8]
            if (date in userdaycal30):
                userdaycal30[date] += i[1]
        tmp1 = list(userdaycal30)
        tmp2 = list(userdaycal30.values())

        for i in range(len(tmp1)):
            userdaycallist30.append([tmp1[i], tmp2[i]])

        print(userdaycallist30)

        db.commit()
        db.close()

        return JsonResponse({'code': '0000', 'day15': userdaycallist15, 'day30': userdaycallist30}, status=200)


@csrf_exempt
def saveFood(request):
    if request.method == 'POST':
        food_name = request.POST.get('food_name', '')
        time = request.POST.get('time', 0)
        user = request.POST.get('id', '')
        sex = request.POST.get('sex', '')
        height = int(request.POST.get('height'))
        weight = int(request.POST.get('weight'))
        age = int(request.POST.get('age'))

        db = pymysql.connect(
            user='root',
            passwd='1234',
            host='localhost',
            db='food',
            charset='utf8'
        )
        cursor = db.cursor()
        sql = "select calorie, carbo, protein, fat, id from foods where name = %s"
        cursor.execute(sql, food_name)
        result = cursor.fetchall()
        calorie = result[0][0]
        carbo = result[0][1]
        protein = result[0][2]
        fat = result[0][3]
        id = result[0][4]
        sql = "insert into user_food (user, food, food_name, tim, sex, weight, height, age, calorie, carbo, " \
                 "protein, fat) values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
        cursor.execute(sql, (user, id, food_name, time, sex, weight, height, age,
                                    calorie, carbo, protein, fat))
        db.commit()
        db.close()

        return JsonResponse({'code': '0000'}, status=200)

@csrf_exempt
def deleteFood(request):
    if request.method == 'POST':
        food_name = request.POST.get('food', '')
        time = request.POST.get('time', 0)
        user = request.POST.get('id', '')

        print(food_name, time, user)

        db = pymysql.connect(
            user='root',
            passwd='1234',
            host='localhost',
            db='food',
            charset='utf8'
        )
        cursor = db.cursor()
        sql = "delete from user_food where user = %s and tim = %s and food_name = %s"
        cursor.execute(sql, (user, time, food_name))

        sql = "select food_name, tim, calorie, carbo, protein, fat from user_food where user = %s and tim like %s"
        cursor.execute(sql, (user, "%" + time))
        result = cursor.fetchall()

        db.commit()
        db.close()

        if len(result) == 0:
            return JsonResponse({"code": "0001"})
        else:
            return JsonResponse({"foods": [list(foods) for foods in result], "code": "0000"}, status=200)


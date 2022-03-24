import datetime
import pymysql



db = pymysql.connect(
    user='root',
    passwd='1234',
    host='localhost',
    db='food',
    charset='utf8')

cursor = db.cursor()
#음식이름/성별/몸무게/키/나이/날짜/칼로리
sql = "select food_name, sex, weight, height, age, tim, calorie from user_food"
cursor.execute(sql)
result = cursor.fetchall()


d=datetime.datetime.now()
nowmonth = d.strftime("%Y%m")         #현재 달

monthavgcal = []                      #달 평균 칼로리 map으로 만들거임  달/ 평균 칼로리 / 달의 끼니 수
tmpyear = nowmonth[:4]
tmpmon = nowmonth[4:]
insmon = int(tmpmon)
for i in range(1,13):
    insmon = insmon - 1
    if(insmon < 1):
        tmpyear = str(int(tmpyear)-1)
        insmon=12
    if (insmon/10)<1:
        monthavgcal.append([tmpyear +"0" +str(insmon), 0, 0])
    else:
        monthavgcal.append([tmpyear+str(insmon), 0, 0])

everytotalcalorie=[]     #날짜 / 칼로리 더한양 / 음식개수 /  나이

#하루 칼로리 계산한거 뭐로저장할지 생각
#그걸로 한달계산

onemealdate="111111111"     #한끼 먹은 날짜
onemealdatecal=0            #한끼 칼로리
onemealfoodcnt=0            #한끼 음식 개수
index=0
for i in result:

    date=str(i[5])
    year=date[:4]
    month=date[4:6]
    day=date[6:8]
    hour=date[8:]
    if(onemealdate!=date):  #날짜가 달라지면 새로
        everytotalcalorie.append([onemealdate, onemealdatecal, onemealfoodcnt, i[4]])
        onemealfoodcnt=0
        onemealdate=date
        onemealdatecal=0
    onemealfoodcnt+=1
    onemealdatecal=onemealdatecal+i[6]
    if((len(result)-1)==index):
        everytotalcalorie.append([onemealdate, onemealdatecal, onemealfoodcnt, i[4]])
    index += 1

everytotalcalorie.pop(0)     #맨앞 쓰레기값 제거
for i in everytotalcalorie:
      for j in monthavgcal:
          if(i[0][:6]==j[0]):
              j[2]+=1
              j[1]+=i[1]

for i in monthavgcal:
    if(i[2]!=0):
        i[1]=int(i[1]/i[2])
print(everytotalcalorie)
print(monthavgcal)          #이녀석 리재홍한테 보내기


age10list=[]        #10~80대 한끼 평균 칼로리 리스트
age20list=[]
age30list=[]
age40list=[]
age50list=[]
age60list=[]
age70list=[]
age80list=[]
age10avgcal=0       #10~80대 한끼 평균 칼로리
age20avgcal=0
age30avgcal=0
age40avgcal=0
age50avgcal=0
age60avgcal=0
age70avgcal=0
age80avgcal=0

for i in everytotalcalorie:
    if(i[3]>80):
        age80list.append(i[1])
    elif(i[3]>70):
        age70list.append(i[1])
    elif(i[3]>60):
        age60list.append(i[1])
    elif(i[3]>50):
        age50list.append(i[1])
    elif (i[3] > 40):
        age40list.append(i[1])
    elif (i[3] > 30):
        age30list.append(i[1])
    elif (i[3] > 20):
        age20list.append(i[1])
    elif (i[3] > 10):
        age10list.append(i[1])
    else:
        continue
if(len(age10list)!=0):
    age10avgcal=int(sum(age10list)/len(age10list))
if(len(age20list)!=0):
    age20avgcal=int(sum(age20list)/len(age20list))
if (len(age30list) != 0):
    age30avgcal=int(sum(age30list)/len(age30list))
if(len(age40list)!=0):
    age40avgcal=int(sum(age40list)/len(age40list))
if(len(age50list)!=0):
    age50avgcal=int(sum(age50list)/len(age50list))
if(len(age60list)!=0):
    age60avgcal=int(sum(age60list)/len(age60list))
if(len(age70list)!=0):
    age70avgcal=int(sum(age70list)/len(age70list))
if(len(age80list)!=0):
    age80avgcal=int(sum(age80list)/len(age80list))

agecalavglist=[]
agecalavglist.append(age10avgcal)
agecalavglist.append(age20avgcal)
agecalavglist.append(age30avgcal)
agecalavglist.append(age40avgcal)
agecalavglist.append(age50avgcal)
agecalavglist.append(age60avgcal)
agecalavglist.append(age70avgcal)
agecalavglist.append(age80avgcal)
print(agecalavglist)

from impala.dbapi import connect
from impala.util import as_pandas
import time
import sys
import json
from elasticsearch import Elasticsearch

# es = Elasticsearch([{'host':'10.82.69.237', 'port':9200}])
es = Elasticsearch([{'host':'10.248.36.65', 'port':9200}]) # D5



start = time.time()


try:
    conn = connect(host='10.240.1.101',port=21050)
    sys.stdout.write("Impala Test: Connected to Impala Tables! (Loc: US- Roch; IP:10.240.1.101)\n")
except:
    sys.stderr.write("****Impala ALERT: Error connecting to Impala Tables! (Loc: US - Roch; IP:10.240.1.101)****\n")
    exit()

cur = conn.cursor()


describeQuery = "describe venusq3.venusq_inventory_master"
describeQueryResult = []
cur.execute(describeQuery)
for row in cur.fetchall():
	describeQueryResult.append(row[0])

#Products = apk, ar8, lhc, aca, c1b, cep, cbd, kcf, xgt, x8h, cbg, cbf, xfh


count = 0

query1 = "select * from venusq3.venusq_inventory_master where year=2016 and month=3 and procid=\"6400\" and product in ('lhc', 'xgt', 'x8h', 'xfh', 'lha')"
cur.execute(query1)
for row in cur:
	count+=1
	mydict = {}
	mydict["Product"] 		= 	row[describeQueryResult.index("product")]
	mydict["HSATRIAL"] 		= 	row[describeQueryResult.index("hsatrial")]
	mydict["FEATTBLVER"] 	= 	row[describeQueryResult.index("feattblver")]
	mydict["TESTCYCLE"] 	= 	row[describeQueryResult.index("testcycle")]
	mydict["Version"]  		= 	row[describeQueryResult.index("vq_version")]
	mydict["EVENTCODE"] 	= 	row[describeQueryResult.index("eventcode")]
	mydict["PFCODE"] 		= 	row[describeQueryResult.index("pfcode")]
	mydict["TOP_PN"] 		= 	row[describeQueryResult.index("toppartnum")]
	mydict["HDCTYPE"] 		= 	"" 						# Check
	mydict["MICROLEVEL"] 	= 	row[describeQueryResult.index("microlevel")]
	mydict["TESTCODE"] 		= 	row[describeQueryResult.index("testpgmver")]  # Check this
	mydict["PFSUBCODE"] 	= 	row[describeQueryResult.index("pfsubcode")]
	mydict["MULTIDEF"] 		= 	row[describeQueryResult.index("multidef")]
	mydict["MFGID"] 		= 	row[describeQueryResult.index("mfgid")]
	mydict["HDDSN"] 		= 	row[describeQueryResult.index("serial")]
	mydict["CELLID"] 		= 	row[describeQueryResult.index("cellid")]
	mydict["RWKDISP"] 		= 	row[describeQueryResult.index("rwkdisp")]
	mydict["CHAMBERID"] 	= 	row[describeQueryResult.index("chamberid")]
	mydict["STARTDATE"] 	= 	row[describeQueryResult.index("startdate")]
	mydict["TESTERID"] 		= 	row[describeQueryResult.index("testerid")]
	mydict["JOBTIME"] 		= 	row[describeQueryResult.index("jobtime")]
	mydict["TESTCODEC"] 	= 	""  				# Check this
	mydict["NEXTPROCID"] 	= 	row[describeQueryResult.index("nextprocid")]
	mydict["LINEID"] 		= 	row[describeQueryResult.index("lineid")]
	mydict["RECLEN"] 		= 	row[describeQueryResult.index("reclen")]
	mydict["PROCID"] 		= 	row[describeQueryResult.index("procid")]
	mydict["PARTFLAG"] 		= 	row[describeQueryResult.index("partflag")]
	mydict["MFGID_1"] 		= 	row[describeQueryResult.index("mfgid")][0]
	mydict["MFGID_2"] 		= 	row[describeQueryResult.index("mfgid")][1]
	mydict["MFGID_3"] 		= 	row[describeQueryResult.index("mfgid")][2]
	mydict["MFGID_4"] 		= 	row[describeQueryResult.index("mfgid")][3]
	mydict["MFGID_5"] 		= 	row[describeQueryResult.index("mfgid")][4]
	mydict["FILENAME"] 		= 	"BDP"
	mydict["MTYPE"] 		= 	row[describeQueryResult.index("mtype")]
	mydict["HDDTRIAL"] 		= 	row[describeQueryResult.index("hddtrial")]
	mydict["LOCID"] 		= 	row[describeQueryResult.index("locid")]
	mydict["RESERVED"] 		= 	"" # Check 
	mydict["FILLER"] 		= 	"" # Check
	mydict["TESTCODESuff"] 	= 	"" #Check
	mydict["STWERASFLG"] 	= 	row[describeQueryResult.index("stwerasflg")]
	mydict["HDDSNENDDATE"] 	= 	str(row[describeQueryResult.index("serial")])+str(row[describeQueryResult.index("enddate")])
	mydict["ENDDATE"] 		= 	row[describeQueryResult.index("enddate")]
	mydict["PARTID"] 		= 	row[describeQueryResult.index("partid")]
	mydict["PCPGMVER"] 		= 	row[describeQueryResult.index("pcpgmver")]
	mydict["FEATPGMVER"] 	= 	row[describeQueryResult.index("featpgmver")]
	mydict["TESTERTYPE"] 	= 	row[describeQueryResult.index("testertype")]
	mydict["TEAMID"] 		= 	row[describeQueryResult.index("teamid")]
	mydict["FIRSTYLDEXP"] 	= 	"" # Check
	mydict["DISABLEHD"] 	= 	row[describeQueryResult.index("disablehd")]
	try:
		es.index(index='mfg', doc_type='CcbStdHdr',id=mydict["HDDSNENDDATE"], body=mydict)
	except Exception as e: 
		print(str(e))
		print("Cannot put this to elasticsearch ")
		print(json.dumps(mydict,indent=4))
	finally:
		print( str(count) + " " + str(mydict["HDDSNENDDATE"]))
	



cur.close()
conn.close()

print("It took " + str(time.time()- start) + " seconds to process " + str(count) + " files\n")

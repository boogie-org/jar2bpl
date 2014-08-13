type ref;
type javaType;
type Field $GenericType__0;
type $heap_type = <$GenericType__0>[ref,Field $GenericType__0]$GenericType__0;
type boolArrHeap_type = [ref][int]bool;
type refArrHeap_type = [ref][int]ref;
type realArrHeap_type = [ref][int]int;
type intArrHeap_type = [ref][int]int;
const unique $null : ref;
const unique $type : Field javaType;
const unique $alloc : Field bool;
const { :sourceloc "Object.java",-1,-1,-1,-1 } unique java.lang.Object : javaType extends  complete;
const { :sourceloc "FalsePositives.java",-1,-1,-1,-1 } unique FalsePositives : javaType extends  unique java.lang.Object complete;
const { :sourceloc "Serializable.java",-1,-1,-1,-1 } unique java.io.Serializable : javaType extends  unique java.lang.Object complete;
const { :sourceloc "Comparable.java",-1,-1,-1,-1 } unique java.lang.Comparable : javaType extends  unique java.lang.Object complete;
const { :sourceloc "CharSequence.java",-1,-1,-1,-1 } unique java.lang.CharSequence : javaType extends  unique java.lang.Object complete;
const { :sourceloc "String.java",-1,-1,-1,-1 } unique java.lang.String : javaType extends  unique java.lang.CharSequence, unique java.lang.Object, unique java.lang.Comparable, unique java.io.Serializable complete;
const unique $StringConst0 : ref extends  complete;
const unique $StringConst1 : ref extends  complete;
const unique $StringConst2 : ref extends  complete;
var $heap : $heap_type;
var $intArrayType : javaType;
var $charArrayType : javaType;
var $boolArrayType : javaType;
var $byteArrayType : javaType;
var $longArrayType : javaType;
var $arrSizeHeap : [ref]int;
var $stringSizeHeap : [ref]int;
var $boolArrHeap : boolArrHeap_type;
var $refArrHeap : refArrHeap_type;
var $realArrHeap : realArrHeap_type;
var $intArrHeap : intArrHeap_type;
var int$FalsePositives$countSource0 : Field int;
function $arrayType(t:javaType) returns ($ret:javaType);
function $intToReal(x:int) returns ($ret:real);
function $intToBool(x:int) returns ($ret:bool) { (if x == 0 then false else true) }
function $refToBool(x:ref) returns ($ret:bool) { (if x == $null then false else true) }
function $boolToInt(x:bool) returns ($ret:int) { (if x == true then 1 else 0) }
function $cmpBool(x:bool, y:bool) returns ($ret:int);
function $cmpRef(x:ref, y:ref) returns ($ret:int);
function $cmpReal(x:real, y:real) returns ($ret:int) { (if x > y then 1 else (if x < y then -1 else 0)) }
function $cmpInt(x:int, y:int) returns ($ret:int) { (if x > y then 1 else (if x < y then -1 else 0)) }
function $bitOr(x:int, y:int) returns ($ret:int);
function $bitAnd(x:int, y:int) returns ($ret:int);
function $xorInt(x:int, y:int) returns ($ret:int);
function $shlInt(x:int, y:int) returns ($ret:int);
function $ushrInt(x:int, y:int) returns ($ret:int);
function $shrInt(x:int, y:int) returns ($ret:int);
procedure $new(obj_type:javaType) returns ($obj:ref);        requires $heap[$obj,$alloc] == false;    ensures $heap[$obj,$type] == obj_type;    ensures $heap[$obj,$alloc] == true;    ensures $obj != $null;

procedure java.lang.Object$java.lang.Object$clone$43($this:ref) returns ($other:ref);    ensures $heap[$other,$type] == $heap[$this,$type];    ensures $heap[$other,$alloc] == true;        ensures $other != $null;

procedure int$java.lang.String$compareTo$87($this:ref, $other:ref) returns ($return:int);    ensures ($this == $other ==> $return == 1) && ($this != $other ==> $return == 0);    

procedure void$FalsePositives$FalsePositive02$1889($this:ref, $in_parameter__0:ref) returns ($exception:ref);    modifies $heap;

procedure java.lang.String$java.lang.Object$toString$44($this:ref) returns ($return:ref, $exception:ref);    

procedure void$FalsePositives$$la$init$ra$$1890($this:ref) returns ($exception:ref);    

procedure void$java.lang.Object$$la$init$ra$$38($this:ref) returns ($exception:ref);    

implementation void$FalsePositives$FalsePositive02$1889($this:ref, $in_parameter__0:ref) returns ($exception:ref){
    
var temp$25 : int;    
var temp$710 : int;    
var htmlTag2 : ref;    
var temp$36 : ref;    
var temp$47 : int;    
var temp$58 : int;    
var temp$811 : int;    
var temp$03 : ref;    
var temp$69 : ref;    
var this1 : ref;    
var temp$14 : int;
    assume $this != $null;
    this1 := $this;
    htmlTag2 := $in_parameter__0;
    assert { :sourceloc "FalsePositives.java",67,7,67,47 } htmlTag2 != $null;
    call temp$03, $exception := java.lang.String$java.lang.Object$toString$44(htmlTag2);
    assert { :sourceloc "FalsePositives.java",67,7,67,47 } temp$03 != $null;
    call temp$14 := int$java.lang.String$compareTo$87(temp$03, $StringConst0);
    if (temp$14 == 0) {
        assert { :sourceloc "FalsePositives.java",67,7,67,47 } true;
        goto block1;
    } else {
        assert { :sourceloc "FalsePositives.java",67,7,67,47 } true;
    }
    goto block2;
  block1:
    temp$25 := 1;
    assert { :sourceloc "FalsePositives.java",69,4,69,14 } this1 != $null;
    $heap := $heap[this1,int$FalsePositives$countSource0 := temp$25];
    goto block3;
  block2:
    assert { :sourceloc "FalsePositives.java",71,12,71,49 } htmlTag2 != $null;
    call temp$36, $exception := java.lang.String$java.lang.Object$toString$44(htmlTag2);
    assert { :sourceloc "FalsePositives.java",71,12,71,49 } temp$36 != $null;
    call temp$47 := int$java.lang.String$compareTo$87(temp$36, $StringConst1);
    if (temp$47 == 0) {
        assert { :sourceloc "FalsePositives.java",71,12,71,49 } true;
        goto block4;
    } else {
        assert { :sourceloc "FalsePositives.java",71,12,71,49 } true;
    }
    goto block5;
  block4:
    temp$58 := 2;
    assert { :sourceloc "FalsePositives.java",73,4,73,14 } this1 != $null;
    $heap := $heap[this1,int$FalsePositives$countSource0 := temp$58];
    goto block6;
  block5:
    assert { :sourceloc "FalsePositives.java",75,12,75,49 } htmlTag2 != $null;
    call temp$69, $exception := java.lang.String$java.lang.Object$toString$44(htmlTag2);
    assert { :sourceloc "FalsePositives.java",75,12,75,49 } temp$69 != $null;
    call temp$710 := int$java.lang.String$compareTo$87(temp$69, $StringConst2);
    if (temp$710 == 0) {
        assert { :sourceloc "FalsePositives.java",75,12,75,49 } true;
        goto block7;
    } else {
        assert { :sourceloc "FalsePositives.java",75,12,75,49 } true;
    }
    goto block8;
  block7:
    temp$811 := 3;
    assert { :sourceloc "FalsePositives.java",77,4,77,14 } this1 != $null;
    $heap := $heap[this1,int$FalsePositives$countSource0 := temp$811];
  block8:
  block6:
  block3:
    return;
}


implementation void$FalsePositives$$la$init$ra$$1890($this:ref) returns ($exception:ref){
    
var this12 : ref;
    assume $this != $null;
    this12 := $this;
    call $exception := void$java.lang.Object$$la$init$ra$$38(this12);
    return;
}



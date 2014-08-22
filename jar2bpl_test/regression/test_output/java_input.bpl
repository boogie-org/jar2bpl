type ref;
type javaType;
type Field $GenericType__0;
type $heap_type = <$GenericType__0>[ref,Field $GenericType__0]$GenericType__0;
type boolArrHeap_type = [ref][int]bool;
type refArrHeap_type = [ref][int]ref;
type realArrHeap_type = [ref][int]int;
type intArrHeap_type = [ref][int]int;
const unique $type : Field javaType;
const unique $alloc : Field bool;
const unique $null : ref;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } unique java.lang.Object : javaType extends  complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } unique java.io.Serializable : javaType extends  unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } unique FalsePositives06$FilterMap : javaType extends  unique java.io.Serializable, unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } unique FalsePositives06 : javaType extends  unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } unique FalsePositives06$ContextFilterMaps : javaType extends  unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } unique java.lang.Throwable : javaType extends  unique java.io.Serializable, unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } unique java.lang.Exception : javaType extends  unique java.lang.Throwable complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } unique java.lang.RuntimeException : javaType extends  unique java.lang.Exception complete;
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
var FalsePositives06$FalsePositives06$FilterMap$this$0236 : Field ref;
var java.lang.Object$FalsePositives06$ContextFilterMaps$lock234 : Field ref;
var FalsePositives06$FilterMap$lp$$rp$$FalsePositives06$ContextFilterMaps$array235 : Field ref;
var int$FalsePositives06$ContextFilterMaps$insertPoint0 : Field int;
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
axiom (forall t : javaType :: $heap[$null,$type] <: t);
procedure $new(obj_type:javaType) returns ($obj:ref);    ensures $heap[$obj,$type] == obj_type;    requires $heap[$obj,$alloc] == false;        ensures $obj != $null;    ensures $heap[$obj,$alloc] == true;

procedure java.lang.Object$java.lang.Object$clone$43($this:ref) returns ($other:ref);        ensures $heap[$other,$alloc] == true;    ensures $other != $null;    ensures $heap[$other,$type] == $heap[$this,$type];

procedure int$java.lang.String$compareTo$87($this:ref, $other:ref) returns ($return:int);    

procedure void$FalsePositives06$FilterMap$$la$init$ra$$1889($this:ref, $in_parameter__0:ref) returns ($exception:ref);    modifies $heap;

procedure void$java.lang.Object$$la$init$ra$$38($this:ref) returns ($exception:ref);    

procedure FalsePositives06$FilterMap$lp$$rp$$FalsePositives06$ContextFilterMaps$asArray$1890($this:ref) returns ($return:ref, $exception:ref);    

procedure void$FalsePositives06$ContextFilterMaps$$la$init$ra$$1891($this:ref) returns ($exception:ref);    modifies $heap, $arrSizeHeap;

procedure void$FalsePositives06$$la$init$ra$$1892($this:ref) returns ($exception:ref);    

implementation void$FalsePositives06$FilterMap$$la$init$ra$$1889($this:ref, $in_parameter__0:ref) returns ($exception:ref){
    
var this1 : ref;    
var temp$02 : ref;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } $this != $null;
    assume $heap[$in_parameter__0,$type] <: FalsePositives06;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",8,-1,-1,-1 } true;
    this1 := $this;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",8,-1,-1,-1 } true;
    temp$02 := $in_parameter__0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",8,-1,-1,-1 } true;
    call $exception := void$java.lang.Object$$la$init$ra$$38(this1);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",8,-1,-1,-1 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",8,-1,-1,-1 } this1 != $null;
    $heap := $heap[this1,FalsePositives06$FalsePositives06$FilterMap$this$0236 := temp$02];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",8,-1,-1,-1 } true;
    return;
}


implementation FalsePositives06$FilterMap$lp$$rp$$FalsePositives06$ContextFilterMaps$asArray$1890($this:ref) returns ($return:ref, $exception:ref){
    
var temp$26 : ref;    
var temp$15 : ref;    
var temp$04 : ref;    
var this3 : ref;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } $this != $null;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",37,6,41,6 } true;
    this3 := $this;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",38,24,38,27 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",38,24,38,27 } this3 != $null;
    temp$04 := $heap[this3,java.lang.Object$FalsePositives06$ContextFilterMaps$lock234];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",38,24,38,27 } true;
  block1:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",38,24,38,27 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",39,21,39,25 } true;
    temp$15 := $heap[this3,FalsePositives06$FilterMap$lp$$rp$$FalsePositives06$ContextFilterMaps$array235];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",38,10,40,10 } true;
  block3:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",39,14,39,26 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",39,14,39,26 } true;
    $return := temp$15;
    return;
  block2:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",39,14,39,26 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",39,14,39,26 } true;
    if ($exception != $null) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",39,14,39,26 } true;
    } else {
        assert { :noverify } true;
        call $exception := $new(java.lang.RuntimeException);
        goto block2;
    }
    assume $heap[$exception,$type] <: java.lang.Throwable;
    temp$26 := $exception;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",38,10,40,10 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",38,10,40,10 } true;
    $exception := temp$26;
    goto block2;
  block4:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",38,10,40,10 } true;
}


implementation void$FalsePositives06$ContextFilterMaps$$la$init$ra$$1891($this:ref) returns ($exception:ref){
    
var $fakelocal_0 : ref;    
var $fakelocal_1 : ref;    
var temp$210 : int;    
var this7 : ref;    
var temp$19 : ref;    
var temp$08 : ref;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } $this != $null;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",12,-1,-1,-1 } true;
    this7 := $this;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",12,-1,-1,-1 } true;
    call $exception := void$java.lang.Object$$la$init$ra$$38(this7);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",12,-1,-1,-1 } true;
    call $fakelocal_0 := $new(java.lang.Object);
    temp$08 := $fakelocal_0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",13,34,13,45 } true;
    call $exception := void$java.lang.Object$$la$init$ra$$38(temp$08);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",13,6,13,46 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",13,6,13,46 } this7 != $null;
    $heap := $heap[this7,java.lang.Object$FalsePositives06$ContextFilterMaps$lock234 := temp$08];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",13,6,13,46 } true;
    call $fakelocal_1 := $new($arrayType(FalsePositives06$FilterMap));
    $arrSizeHeap := $arrSizeHeap[$fakelocal_1 := 0];
    temp$19 := $fakelocal_1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",21,6,21,50 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",21,6,21,50 } this7 != $null;
    $heap := $heap[this7,FalsePositives06$FilterMap$lp$$rp$$FalsePositives06$ContextFilterMaps$array235 := temp$19];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",21,6,21,50 } true;
    temp$210 := 0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",32,6,32,33 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",32,6,32,33 } this7 != $null;
    $heap := $heap[this7,int$FalsePositives06$ContextFilterMaps$insertPoint0 := temp$210];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",32,6,32,33 } true;
    return;
}


implementation void$FalsePositives06$$la$init$ra$$1892($this:ref) returns ($exception:ref){
    
var this11 : ref;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",-1,-1,-1,-1 } $this != $null;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",6,-1,-1,-1 } true;
    this11 := $this;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",6,-1,-1,-1 } true;
    call $exception := void$java.lang.Object$$la$init$ra$$38(this11);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp06/FalsePositives06.javaFalsePositives06.java",6,-1,-1,-1 } true;
    return;
}



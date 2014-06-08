type wurst;

/*
type ref;
type javaType;
const unique $null : ref;


type Field $GenericType__0;
var $heap : <$GenericType__0>[ref,Field $GenericType__0]$GenericType__0;

//$type is used to store the type of a java object on the heap.
//E.g., String s will have $heap[s,$type]==string
const unique $type : Field javaType;
//$heap[s,$alloc] is true if s is properly allocated
const unique $alloc : Field bool ;


var $boolArrayType : javaType;
var $byteArrayType : javaType;
var $longArrayType : javaType;

//an array that stores the size of java arrays.
var $arrSizeHeap : [ref]int;

//an array that stores the size of java string.
var $stringSizeHeap : [ref]int;



var $boolArrHeap : [ref][int]bool;
var $refArrHeap : [ref][int]ref;
var $realArrHeap : [ref][int]int;
var $intArrHeap : [ref][int]int;


//helper functions to cast between types.
//this is necessary because java bytecode does not distinguish between
//int and bool.
function $intToReal(x:int) returns ($ret:real);
function $intToBool(x:int) returns ($ret:bool) { (if x == 0 then false else true) }
function $refToBool(x:ref) returns ($ret:bool) { (if x == $null then false else true) }
function $boolToInt(x:bool) returns ($ret:int) { (if x == true then 1 else 0) }


function $arrayType(t:javaType) returns ($ret:javaType);
var $intArrayType : javaType;
var $charArrayType : javaType;

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

*/



















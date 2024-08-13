<template>
    <!-- HTML요소의 동적 값 표시 -->
     <!-- html요소 안에 중괄호 2번 사용함으로서 script에 정의된 변수값을 사용한다 -->
     <h1>hello world {{language}}</h1>

     <!-- :value문법을 사용하면 단방향 데이터 바인딩 -->
     <input type="text" :value="value1">
     <!-- v-model을 사용하면 양방향 데이터 바인딩 -->
      <input type="text" v-model="value2">
      <button @click="showValue">변수 변경사항 확인</button>
     <br>
     <input type="number" v-model="count">
      <h1>{{count}}</h1>
     <button @click="increment">숫자 증가</button>
     <br>
     <button @click="refresh">초기화</button>
     <br>
     <h1>{{ddoubleCount}}</h1>
    
</template>

<script>
    export default{
        data(){
            return{
                language:"java",
                value1:"python",
                value2:"java",
                count:0
            }
        },
        created() {
            alert("created 함수 호출")
        },
        mounted() {
            alert("mounted 함수 호출")
        },
        unmounted() {
            alert("unmounted 함수 호출")
        },
        // computed는 종속된 반응형 데이터가 변경될때에 함수를 다시 실행하여 값을 계산하는 함수
        computed: {
            doubleCount(){
                return this.count*2
            },
            ddoubleCount(){
                return this.doubleCount*2
            }
        },
        methods:{
            showValue(){
                alert(this.value2)
            },
            increment(){
                // alert(this.count)
                this.count+=1
            },
            refresh(){
                this.count=0
            }
        }
    }
</script>
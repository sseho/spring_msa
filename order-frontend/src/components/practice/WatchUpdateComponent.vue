<template>
    <h1>watch 예제</h1>
    <div>count : {{count}}</div>
    <v-btn @click="incrementCount">increment count</v-btn>
    <h1>update 예제</h1>
    <div>message : {{message}}</div>
    <v-btn @click="updateMessage">update message</v-btn>
</template>

<script>
    export default{
        data(){
            return{
                count:0,
                message:"hello java",
                
            }
        },
        // 특정 변수값의 변경사항을 인지하는 hook함수
        watch:{
            // 인지하고자 하는 변수값과 일치하는 함수명(특정한 변수값 추적)
            count(){
                alert("카운트")
            }
        },
        // dom객체(화면)의 변경사항을 인지하는 hook함수
        updated() {
            alert("dom 요소 변경")
        },
        computed: {
            
        },
        methods:{
            incrementCount(){
                this.count++;
            },
            updateMessage(){
              this.message="hello python"  
            }
        }
    }
</script>
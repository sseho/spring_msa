<template>
    <h1>화면 라우팅 테스트</h1>
    <br>
    <v-btn @click="spaMove1">modeling 페이지로 이동(url)</v-btn><br>
    <v-btn @click="spaMove2">modeling 페이지로 이동(name)</v-btn><br>
    <v-btn @click="hrefMove">modeling 페이지로 이동(href)</v-btn><br>
    <v-btn @click="reloadPage">현재화면 reload</v-btn>
</template>

<script>
    export default{
        data(){
            return{
                
            }
        },
        created() {
            
        },
        watch:{
            
        },
        updated() {

        },
        computed: {
            
        },
        methods:{
            spaMove1(){
                this.$router.push("/practice/modeling")
            },
            spaMove2(){
                this.$router.push({name:"ModelingComponent"})
            },
            hrefMove(){
                window.location.href='/practice/modeling'
            },
            reloadPage(){
                window.location.reload();
            },
        }
    }
</script>
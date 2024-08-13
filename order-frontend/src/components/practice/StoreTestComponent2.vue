<template>
    <div>StoreTestComponent2</div>
    <p>count : {{getCount}}</p>
    
    <v-btn @click="incrementCount" color="success">incrementCount</v-btn>
    <input type="text" v-model="message">
    <v-btn color="success" @click="updateMessage">update message</v-btn>
</template>

<script>
import { mapGetters } from 'vuex';
    export default{
        data(){
            return{
                count:0,
                message:""
            }
        },
        created(){

        },
        watch:{
            
        },
        updated() {

        },
        computed: {
            ...mapGetters(['getCount']),
            // ...mapGetters(['getMessage']),
        },
        methods:{
            incrementCount(){
                // this.count++;
                this.$store.dispatch('incrementCount')
            },
            updateMessage(){
                this.$store.dispatch('updateMessage',this.message)
            }
        }
    }
</script>
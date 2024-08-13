<template>
    <div>StoreTestComponent3</div>
    <p>count : {{getCount}}</p>
    <p>message : {{getMessage}}</p>
</template>

<script>
import { mapGetters } from 'vuex';
    export default{
        data(){
            return{
                count:0
            }
        },
        created(){

        },
        watch:{
            
        },
        updated() {

        },
        computed: {
            ...mapGetters(['getCount','getMessage'])
        },
        methods:{

        }
    }
</script>
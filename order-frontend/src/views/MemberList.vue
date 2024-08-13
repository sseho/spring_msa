<template>
    <v-container>
        <v-row>
            <v-col>
                <v-card>
                    <v-card-title class="text-center text-h5">
                        회원목록
                    </v-card-title>
                    <v-card-text>
                        <v-data-table :headers="tableHeaders" :items="memberList" />
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            tableHeaders: [{ title: 'ID', key: 'id', align: 'start' },
            { title: '이름', key: 'name', align: 'start' },
            { title: '이메일', key: 'email', align: 'start' },
            { title: '주문수량', key: 'orderCount', align: 'start' },],
            memberList: [],
        }
    },
    async created() {
        try {
            // const token = localStorage.getItem('token')
            // const headers = { Authorization: `Bearer ${token}` }
            // {"headers": {"Authorization" : `Bearer 토큰값`}}
            // const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member/list`, { headers })

            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member-service/member/list`)
            this.memberList = response.data.result.content
        } catch (e) {
            console.log(e.response.data);
        }
    },
    watch: {

    },
    updated() {

    },
    computed: {

    },
    methods: {

    }
}
</script>
<style>
.text-center{
    text-align: center;
}
</style>
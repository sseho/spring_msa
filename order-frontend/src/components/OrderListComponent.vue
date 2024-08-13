<template>
    <v-container>
        <v-row justify="center">
            <v-col>
                <v-card-title class="text-center text-h5">
                    주문목록
                </v-card-title>

                <v-card-text>
                    <v-data-table :headers="tableHeaders" :items="orderList" class="elevation-1" show-expand>
                        <template v-slot:[`item.actions`]="{ item }">
                            <v-btn color="red" v-if="isAdmin && item.orderStatus === 'ORDERED'"
                                @click.stop="cancelOrder(item.orderingId)" size=small>
                                CANCEL
                            </v-btn>
                        </template>

                        <template v-slot:expanded-row="{ item }">
                            <v-row>
                                <v-col>
                                    <v-list-item v-for="orderItem in item.orderDetails" :key="orderItem.detailId">
                                        <v-list-item-content>
                                            <v-list-item-title>
                                                {{ orderItem.productName }}  {{ orderItem.productCount }} 개
                                            </v-list-item-title>
                                        </v-list-item-content>
                                    </v-list-item>
                                </v-col>
                            </v-row>
                        </template>

                    </v-data-table>
                </v-card-text>

            </v-col>
        </v-row>
    </v-container>
</template>
<script>
import axios from 'axios';
export default {
    props: ['isAdmin'],
    data() {
        return {
            orderList: [],
            tableHeaders: [
                { title: 'ID', key: 'orderingId', align: 'start' },
                { title: '회원 EMAIL', key: 'memberEmail', align: 'start' },
                { title: '주문상태', key: 'orderStatus', align: 'start' },
                { title: 'ACTION', key: 'actions', align: 'start' }
            ]
        }
    },
    async created() {
        if (this.isAdmin) { // script에서는 this.admin해줘야함
            // admin이면 모든 주문목록 조회
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/order-service/order/list`);
            this.orderList = response.data.result;
        } else {
            // 일반user면 내주문목록만 조회
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/order-service/order/myorders`);
            this.orderList = response.data.result;
        }

    },
    methods: {
        async cancelOrder(id) {
            try {
                await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/order/${id}/cancel`);
                window.location.reload();
            } catch (e) {
                console.log(e);
            }

        }
    }
}
</script>
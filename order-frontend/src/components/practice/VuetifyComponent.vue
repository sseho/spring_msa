<template>

    <v-app-bar app>
        네비게이션 바
    </v-app-bar>

    <!-- v-container, v-row, v-col -->
    <v-container>
        <v-row>
            <!-- cols는 최대 12 -->
            <v-col cols="8">
                hello world1
            </v-col>
            <v-col cols="4">
                hello world2
            </v-col>
        </v-row>
        <v-row>
            <!-- v-card -->
            <v-card>
                <!-- card-title(제목), card-text(내용) -->
                <v-card-title>
                    안내문구
                </v-card-title>
                <v-card-text>
                    안녕하세요
                </v-card-text>
            </v-card>
        </v-row>
        <v-row>
            <v-simple-table>
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>이메일</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>seho</td>
                        <td>seho@naver.com</td>
                    </tr>
                    <tr>
                        <td>seho2</td>
                        <td>seho2@naver.com</td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-row>
        <v-row>

        </v-row>
    </v-container>
    <v-card>
        <v-card-title>회원가입</v-card-title>
        <v-card-text>
            <v-form>
                <v-text-field label="name" type="text"></v-text-field>
                <v-text-field label="email" type="email"></v-text-field>
                <v-text-field label="password" type="password"></v-text-field>
                <v-btn color="primary">제출하기</v-btn>
            </v-form>
        </v-card-text>
    </v-card>
    <br><br>
    <v-btn @click.stop="cancel()">취소버튼</v-btn>

    <!-- 모달창을 위한 v-dialog -->
     <!-- v-dialog 는 v-model 변수를 기준으로 작동한다-->
    <v-dialog v-model="dialog" max-width="400px">
        <v-card>
            <v-card-title>정말 취소 하시겠습니까?</v-card-title>
            <v-card-actions>
                <v-btn color="primary">확인</v-btn>
                <v-btn color="secondary" @click="dialog=false">취소</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-btn href="/practice/modeling">일반 라우팅</v-btn>
    <v-btn :to="{path: '/practice/modeling'}">SPA라우팅(router기능 활용)</v-btn>
    <v-btn :to="{name: 'ModelingComponent'}">SPA라우팅(name 활용)</v-btn>
</template>

<script>
    export default {
        // 변수설정 : data()함수를 통해 변수 세팅
        data() {
            return {
                dialog: false,//초기값
            }
        },
        // 함수 정의
        methods:{
            cancel(){
                // this는 data에서 정의하는 변수를 지칭할 수 있게 해준다
                this.dialog=true
            }
        }
    }
</script>
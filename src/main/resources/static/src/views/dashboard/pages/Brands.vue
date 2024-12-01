<template>
  <v-container
    id="brands"
    fluid
    tag="section"
  >

    <v-dialog v-model="createModal" max-width="300">
      <v-card>
        <v-card-title class="font-weight-bold">브랜드 생성<v-spacer />
          <v-icon aria-label="Close" @click="closeCreateModal">mdi-close</v-icon>
        </v-card-title>
        <v-card-text class="body-1 text-left">
          <v-form>
            <v-row>
              <v-col class="col">
                <p class="font-weight-bold">브랜드명</p>
              </v-col>
              <v-col class="col">
                <input v-model="createBrandName" placeholder="브랜드명을 입력해주세요."/>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-text class="pb-6 pt-12 text-right">
          <v-btn
              color="success"
              text
              @click="createBrand"
          >
            생성
          </v-btn>
        </v-card-text>
      </v-card>
    </v-dialog>

    <v-dialog v-model="editModal" max-width="300">
      <v-card>
        <v-card-title class="font-weight-bold">브랜드 상세<v-spacer />
          <v-icon aria-label="Close" @click="closeEditModal">mdi-close</v-icon>
        </v-card-title>
        <v-card-text class="body-1 text-left">
          <v-form>
            <v-row>
              <v-col class="col">
                <p class="font-weight-bold">브랜드명</p>
              </v-col>
              <v-col class="col">
                <input v-model="editBrandName"/>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-text class="pb-6 pt-12 text-right">
          <v-btn
              color="success"
              text
              @click="editBrand"
          >
            수정
          </v-btn>
        </v-card-text>
      </v-card>
    </v-dialog>

    <base-material-snackbar
        v-model="snackbar"
        :type="snackbarColor"
        v-bind="{
        ['top']: true,
        ['right']: true
      }"
    >
      <span class="font-weight-bold">{{snackbarMessage}}</span>
    </base-material-snackbar>


    <div>
      <div class="px-6 py-3 ">
        <p class="ml-2 font-weight-bold">브랜드 생성</p>
        <v-btn color="info" target="_blank" @click="openCreateModal">
          <v-icon>mdi-storefront-plus-outline</v-icon>
        </v-btn>
      </div>
    </div>

    <base-material-card icon="mdi-store" title="브랜드 목록" class="px-5 py-3">
      <v-simple-table>
        <thead>
          <tr>
            <th class="primary--text font-weight-bold">
              ID
            </th>
            <th class="primary--text font-weight-bold">
              브랜드명
            </th>
            <th class="primary--text font-weight-bold">
              생성일시
            </th>
            <th class="primary--text font-weight-bold">
              수정/삭제
            </th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="item in brands" :key="item.id">
            <td>{{item.id}}</td>
            <td>{{item.name}}</td>
            <td>{{item.regTs}}</td>
            <td>
              <v-btn
                  color="blue"
                  target="_blank"
                  @click="openEditModal(item)"
              >
                <v-icon>mdi-store-edit-outline</v-icon>
              </v-btn>
              <v-btn
                  color="red"
                  target="_blank"
                  v-bind:disabled="processing"
                  @click="deleteItem(item)"
                  class="ml-1"
              >
                <v-icon>mdi-store-remove-outline</v-icon>
              </v-btn>
            </td>
          </tr>
        </tbody>
      </v-simple-table>
    </base-material-card>
  </v-container>
</template>


<script>
import BaseMaterialTestimony from "../../../components/base/MaterialTestimony.vue";

const API_URL = 'http://localhost:8080/api/v1/brands'

export default {
  name: 'BrandListView',
  components: {BaseMaterialTestimony},
  props: {
    id: {
      type: Number
    },
    name: {
      type: String
    },
    regTs: {
      type: String
    }
  },

  data() {
    return {
      processing: false,
      snackbar: false,
      snackbarColor: 'success',
      snackbarMessage: '',
      brands: [],
      cursorExclusive: 0,
      show: true,
      list: [1, 2, 3],

      createModal: false,
      createBrandName: '',

      editModal: false,
      editBrandId: 0,
      editBrandName: '',
    }
  },

  created() {
    this.fetchData(0)
  },

  methods: {

    openCreateModal() {
      this.createBrandName = ''
      this.createModal = true
    },

    async createBrand() {
      if (confirm(`브랜드를 생성하시겠습니까?`) === true) {

        const url = `${API_URL}`
        const requestOptions = {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ name: this.createBrandName })
        };
        await fetch(url, requestOptions)
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                this.showMessage('info', '생성됐습니다.')
                this.createBrandName = ''
                var next = 0
                if (this.brands.length === 0) {
                  next = 0
                } else {
                  next = this.brands[0].id
                }

                this.fetchData(next)
                this.closeCreateModal()
              } else {
                this.showMessage('error', data.error.message)
              }
            });
      }
    },

    closeCreateModal() {
      this.createBrandName = ''
      this.createModal = false
    },

    openEditModal(item) {
      this.editModal = true
      this.editBrandId = item.id
      this.editBrandName = item.name
    },

    async editBrand() {
      const brand = this.brands.find(it => it.id === this.editBrandId)
      if (confirm(`브랜드를 수정하시겠습니까?`) === true) {

        const url = `${API_URL}/${this.editBrandId}`
        const requestOptions = {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ id: this.editBrandId, name: this.editBrandName })
        };
        await fetch(url, requestOptions)
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                this.showMessage('info', '수정됐습니다.')
                brand.name = this.editBrandName
                this.editBrandId = 0
                this.editBrandName = ''
                this.closeEditModal()
              } else {
                this.showMessage('error', data.error.message)
              }
            });
      }
    },

    closeEditModal() {
      this.editModal = false
    },

    async deleteItem(item) {
      if (confirm(`${item.name} 브랜드를 삭제하시겠습니까?`) === true) {
        this.processing = true
        const url = `${API_URL}/${item.id}`

        const requestOptions = {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({  })
        };
        fetch(url, requestOptions)
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                const idx = this.brands.findIndex(function(it) { return it.id === item.id })
                if (idx > -1) {
                  this.brands.splice(idx, 1)
                }
                this.showMessage('info', '삭제됐습니다.')
                this.processing = false
              } else {
                this.$router.go(0)
              }
            });
      }
    },

    async fetchData(cursorExclusive) {
      const url = `${API_URL}?size=5000&cursorExclusive=${cursorExclusive}`
      await fetch(url)
          .then(response =>  response.json())
          .then(data => {
            if (this.cursorExclusive === 0) {
              this.brands = data.data.items
            } else {
              this.brands.unshift(...data.data.items)
            }
            this.cursorExclusive = data.data.nextCursorExclusive
          })
    },

    showMessage(color, message) {
      this.snackbarMessage = message
      this.snackbarColor = color
      this.snackbar = true
    },
  }
}
</script>

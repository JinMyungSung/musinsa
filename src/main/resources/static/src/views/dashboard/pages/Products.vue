<template>
  <v-container
    id="products"
    fluid
    tag="section"
  >

    <v-dialog v-model="createModal" max-width="300">
      <v-card>
        <v-card-title class="font-weight-bold">상품 생성<v-spacer />
          <v-icon aria-label="Close" @click="closeCreateModal">mdi-close</v-icon>
        </v-card-title>
        <v-card-text class="body-1 text-left">
          <v-form>
            <v-row class="md-12">
              <v-col class="col col-md-12">
                <span class="font-weight-bold">브랜드</span>
                <v-select
                  v-model="createBrandId"
                  :items="brands"
                  item-text="name"
                  item-value="id"
                  density="compact"
                ></v-select>
              </v-col>
            </v-row>
            <v-row class="md-12">
              <v-col class="col col-md-12">
                <span class="font-weight-bold">카테고리</span>
                <v-select
                  v-model="createCategoryId"
                  :items="categories"
                  item-text="name"
                  item-value="id"
                  density="compact"
                ></v-select>
              </v-col>
            </v-row>
            <v-row>
              <v-col class="col">
                <p class="font-weight-bold">상품명</p>
              </v-col>
              <v-col class="col">
                <input v-model="createProductName" placeholder="상품명을 입력해주세요."/>
              </v-col>
            </v-row>
            <v-row>
              <v-col class="col">
                <p class="font-weight-bold">대표가격</p>
              </v-col>
              <v-col class="col">
                <input v-model="createMainPrice" placeholder="대표가격을 입력해주세요."/>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-text class="pb-6 pt-12 text-right">
          <v-btn
              color="success"
              text
              @click="createProduct"
          >
            생성
          </v-btn>
        </v-card-text>
      </v-card>
    </v-dialog>

    <v-dialog v-model="editModal" max-width="300">
      <v-card>
        <v-card-title class="font-weight-bold">상품 상세<v-spacer />
          <v-icon aria-label="Close" @click="closeEditModal">mdi-close</v-icon>
        </v-card-title>
        <v-card-text class="body-1 text-left">
          <v-form>
            <v-row class="md-12">
              <v-col class="col col-md-12">
                <span class="font-weight-bold">브랜드</span>
                <v-select
                  disabled
                  v-model="editBrandId"
                  :items="brands"
                  item-text="name"
                  item-value="id"
                  density="compact"
                ></v-select>
              </v-col>
            </v-row>
            <v-row class="md-12">
              <v-col class="col col-md-12">
                <span class="font-weight-bold">카테고리</span>
                <v-select
                  v-model="editCategoryId"
                  :items="categories"
                  item-text="name"
                  item-value="id"
                  density="compact"
                ></v-select>
              </v-col>
            </v-row>
            <v-row>
              <v-col class="col">
                <p class="font-weight-bold">상품명</p>
              </v-col>
              <v-col class="col">
                <input v-model="editProductName" placeholder="상품명을 입력해주세요."/>
              </v-col>
            </v-row>
            <v-row>
              <v-col class="col">
                <p class="font-weight-bold">대표가격</p>
              </v-col>
              <v-col class="col">
                <input v-model="editMainPrice" placeholder="대표가격을 입력해주세요."/>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-text class="pb-6 pt-12 text-right">
          <v-btn
              color="success"
              text
              @click="editProduct"
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
        <p class="ml-2 font-weight-bold">상품 생성</p>
        <v-btn color="info" target="_blank" @click="openCreateModal">
          <v-icon>mdi-storefront-plus-outline</v-icon>
        </v-btn>
      </div>
    </div>

    <base-material-card icon="mdi-store" title="상품 목록" class="px-5 py-3">
      <section class="paging">
        <v-btn
          color="blue"
          target="_blank"
          @click="currentPage = currentPage - 1; fetchData(currentPage)"
          :disabled = "currentPage === 0"
        >이전</v-btn>
        <span>{{currentPage + 1}} / {{ Math.max(totalPages, currentPage + 1) }}</span>
        &nbsp;
        <v-btn
          color="blue"
          target="_blank"
          @click="currentPage = currentPage + 1; fetchData(currentPage)"
          :disabled = "currentPage + 1 === Math.max(totalPages, currentPage + 1)"
        >다음</v-btn>
      </section>
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
              카테고리명
            </th>
            <th class="primary--text font-weight-bold">
              상품명
            </th>
            <th class="primary--text font-weight-bold">
              대표가격
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
          <tr v-for="item in products" :key="item.id">
            <td>{{item.productId}}</td>
            <td>{{item.brandName}}</td>
            <td>{{item.categoryName}}</td>
            <td>{{item.name}}</td>
            <td>{{item.mainPrice}}</td>
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

const API_URL = 'http://localhost:8080/api/v1'

export default {
  name: 'ProductListView',
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
      categories: [],

      products: [],
      cursorExclusive: 0,
      show: true,
      list: [1, 2, 3],

      currentPage: 0,
      totalPages: 0,

      createModal: false,
      createBrandId: null,
      createCategoryId: null,
      createProductName: '',
      createMainPrice: 0,

      editModal: false,
      editProductId: 0,
      editBrandId: null,
      editCategoryId: null,
      editProductName: '',
      editMainPrice: 0,
    }
  },

  created() {
    this.fetchBrands()
    this.fetchCategories()
    this.fetchData(this.currentPage)
  },

  methods: {

    openCreateModal() {
      this.createProductName = ''
      this.createBrandId = null
      this.createCategoryId = null
      this.createMainPrice = 0
      this.createModal = true
    },

    async createProduct() {
      if (confirm(`상품을 생성하시겠습니까?`) === true) {

        const url = `${API_URL}/products`
        const requestOptions = {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            brandId: this.createBrandId,
            categoryId: this.createCategoryId,
            name: this.createProductName,
            mainPrice: this.createMainPrice,
          })
        };
        await fetch(url, requestOptions)
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                this.showMessage('info', '생성됐습니다.')
                this.fetchData(this.currentPage)
                this.closeCreateModal()
              } else {
                this.showMessage('error', data.error.message)
              }
            });
      }
    },

    closeCreateModal() {
      this.createModal = false
    },

    openEditModal(item) {
      this.editProductId = item.productId
      this.editProductName = item.name
      this.editBrandId = item.brandId
      this.editCategoryId = item.categoryId
      this.editMainPrice = item.mainPrice
      this.editModal = true
    },

    async editProduct() {
      const product = this.products.find(it => it.productId === this.editProductId)
      if (confirm(`상품을 수정하시겠습니까?`) === true) {

        const url = `${API_URL}/products/${product.productId}`
        const requestOptions = {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            id: this.editProductId,
            brandId: this.editBrandId,
            categoryId: this.editCategoryId,
            name: this.editProductName,
            mainPrice: this.editMainPrice,
          })
        };
        await fetch(url, requestOptions)
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                this.showMessage('info', '수정됐습니다.')
                this.fetchData(this.currentPage)
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
      if (confirm(`${item.name} 상품을 삭제하시겠습니까?`) === true) {
        this.processing = true
        const url = `${API_URL}/products/${item.productId}`

        const requestOptions = {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({  })
        };

        fetch(url, requestOptions)
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                this.fetchData(0)
                this.showMessage('info', '삭제됐습니다.')
                this.processing = false
              } else {
                this.$router.go(0)
              }
            });
      }
    },

    async fetchBrands() {
      const url = `${API_URL}/brands?size=5000`
      await fetch(url)
        .then(response =>  response.json())
        .then(data => {
          this.brands = data.data.items
        })
    },

    async fetchCategories() {
      const url = `${API_URL}/categories`
      await fetch(url)
        .then(response =>  response.json())
        .then(data => {
          this.categories = data.data
        })
    },

    async fetchData(page) {
      const url = `${API_URL}/products?size=10&page=${page}`
      await fetch(url)
          .then(response =>  response.json())
          .then(data => {
            this.products = data.data.items
            this.currentPage = data.data.page
            this.totalPages = data.data.totalPages
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

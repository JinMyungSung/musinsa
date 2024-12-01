<template>
  <v-container
    id="dashboard"
    fluid
    tag="section"
  >
    <v-row>
      <v-col
        cols="12"
        lg="4"
        style="height: 800px"
      >
        <h2 class="card-title font-weight-bold">
          과제1)
        </h2>
        <textarea
          style="height: 90%; width: 100%;background-color: white"
          disabled
          v-model="homeWork1"/>
      </v-col>
      <v-col
        cols="12"
        lg="4"
      >
        <h2 class="card-title font-weight-bold">
          과제2)
        </h2>
        <textarea
          style="height: 90%; width: 100%;background-color: white"
          disabled
          v-model="homeWork2"/>
      </v-col>
      <v-col
        cols="12"
        lg="4"
      >
        <h2 class="card-title font-weight-bold">
          과제3)
        </h2>
        <textarea
          style="height: 70%; width: 100%;background-color: white"
          disabled
          v-model="homeWork3"/>
        <v-select
          v-model="editCategoryId"
          :items="categories"
          item-text="name"
          item-value="name"
          density="compact"
          v-on:change="fetchData"
        ></v-select>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  const API_URL = 'http://localhost:8080/api/v1'

  export default {
    name: 'DashboardDashboard',

    data () {
      return {
        editCategoryId: "상의",
        categories: [],
        homeWork1: "",
        homeWork2: "",
        homeWork3: "",
        dailySalesChart: {
          data: {
            labels: ['M', 'T', 'W', 'T', 'F', 'S', 'S'],
            series: [
              [12, 17, 7, 17, 23, 18, 38],
            ],
          },
          options: {
            lineSmooth: this.$chartist.Interpolation.cardinal({
              tension: 0,
            }),
            low: 0,
            high: 50, // creative tim: we recommend you to set the high sa the biggest value + something for a better look
            chartPadding: {
              top: 0,
              right: 0,
              bottom: 0,
              left: 0,
            },
          },
        },
        dataCompletedTasksChart: {
          data: {
            labels: ['12am', '3pm', '6pm', '9pm', '12pm', '3am', '6am', '9am'],
            series: [
              [230, 750, 450, 300, 280, 240, 200, 190],
            ],
          },
          options: {
            lineSmooth: this.$chartist.Interpolation.cardinal({
              tension: 0,
            }),
            low: 0,
            high: 1000, // creative tim: we recommend you to set the high sa the biggest value + something for a better look
            chartPadding: {
              top: 0,
              right: 0,
              bottom: 0,
              left: 0,
            },
          },
        },
        emailsSubscriptionChart: {
          data: {
            labels: ['Ja', 'Fe', 'Ma', 'Ap', 'Mai', 'Ju', 'Jul', 'Au', 'Se', 'Oc', 'No', 'De'],
            series: [
              [542, 443, 320, 780, 553, 453, 326, 434, 568, 610, 756, 895],

            ],
          },
          options: {
            axisX: {
              showGrid: false,
            },
            low: 0,
            high: 1000,
            chartPadding: {
              top: 0,
              right: 5,
              bottom: 0,
              left: 0,
            },
          },
          responsiveOptions: [
            ['screen and (max-width: 640px)', {
              seriesBarDistance: 5,
              axisX: {
                labelInterpolationFnc: function (value) {
                  return value[0]
                },
              },
            }],
          ],
        },
        headers: [
          {
            sortable: false,
            text: 'ID',
            value: 'id',
          },
          {
            sortable: false,
            text: 'Name',
            value: 'name',
          },
          {
            sortable: false,
            text: 'Salary',
            value: 'salary',
            align: 'right',
          },
          {
            sortable: false,
            text: 'Country',
            value: 'country',
            align: 'right',
          },
          {
            sortable: false,
            text: 'City',
            value: 'city',
            align: 'right',
          },
        ],
        items: [
          {
            id: 1,
            name: 'Dakota Rice',
            country: 'Niger',
            city: 'Oud-Tunrhout',
            salary: '$35,738',
          },
          {
            id: 2,
            name: 'Minerva Hooper',
            country: 'Curaçao',
            city: 'Sinaai-Waas',
            salary: '$23,738',
          },
          {
            id: 3,
            name: 'Sage Rodriguez',
            country: 'Netherlands',
            city: 'Overland Park',
            salary: '$56,142',
          },
          {
            id: 4,
            name: 'Philip Chanley',
            country: 'Korea, South',
            city: 'Gloucester',
            salary: '$38,735',
          },
          {
            id: 5,
            name: 'Doris Greene',
            country: 'Malawi',
            city: 'Feldkirchen in Kārnten',
            salary: '$63,542',
          },
        ],
        tabs: 0,
        tasks: {
          0: [
            {
              text: 'Sign contract for "What are conference organizers afraid of?"',
              value: true,
            },
            {
              text: 'Lines From Great Russian Literature? Or E-mails From My Boss?',
              value: false,
            },
            {
              text: 'Flooded: One year later, assessing what was lost and what was found when a ravaging rain swept through metro Detroit',
              value: false,
            },
            {
              text: 'Create 4 Invisible User Experiences you Never Knew About',
              value: true,
            },
          ],
          1: [
            {
              text: 'Flooded: One year later, assessing what was lost and what was found when a ravaging rain swept through metro Detroit',
              value: true,
            },
            {
              text: 'Sign contract for "What are conference organizers afraid of?"',
              value: false,
            },
          ],
          2: [
            {
              text: 'Lines From Great Russian Literature? Or E-mails From My Boss?',
              value: false,
            },
            {
              text: 'Flooded: One year later, assessing what was lost and what was found when a ravaging rain swept through metro Detroit',
              value: true,
            },
            {
              text: 'Sign contract for "What are conference organizers afraid of?"',
              value: true,
            },
          ],
        },
        list: {
          0: false,
          1: false,
          2: false,
        },
      }
    },

    created() {
      this.fetchData()
    },

    methods: {
      complete (index) {
        this.list[index] = !this.list[index]
      },

      async fetchData() {
        await fetch(`${API_URL}/categories`)
          .then(response =>  response.json())
          .then(data => {
            this.categories = data.data
          })

        await fetch(`${API_URL}/home-work-1`)
          .then(response =>  response.json())
          .then(data => {
            this.homeWork1 = data.tsv
          })

        await fetch(`${API_URL}/home-work-2`)
          .then(response =>  response.json())
          .then(data => {
            this.homeWork2 = JSON.stringify(data, null, 4)
          })

        await fetch(`${API_URL}/home-work-3?category=${this.editCategoryId}`)
          .then(response =>  response.json())
          .then(data => {
            this.homeWork3 = JSON.stringify(data, null, 4)
          })
      }
    },
  }
</script>

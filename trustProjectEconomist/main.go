package main

import (
	"fmt"
	"html/template"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"reflect"

	"github.com/PuerkitoBio/goquery"
)

func Docs(w http.ResponseWriter, r *http.Request) {
	data, err := ioutil.ReadFile("organization.html")
	if err == nil {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(data))
	} else {
		w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
	}
}

func Validate(w http.ResponseWriter, r *http.Request) {
	if r.Method == "GET" {
		t, _ := template.ParseFiles("validate.html")
		t.Execute(w, nil)
	} else {
		validatePage(w, r)
	}
}

func Generate(w http.ResponseWriter, r *http.Request) {
	if r.Method == "GET" {
		t, _ := template.ParseFiles("generate.html")
		t.Execute(w, nil)
	} else {
		// Not quite working yet.
		// generatePage(w, r)
		data, err := ioutil.ReadFile("generateTest.html")
		if err == nil {
			w.WriteHeader(http.StatusOK)
			w.Write([]byte(data))
		} else {
			w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
		}
	}
}

func TestPage(w http.ResponseWriter, r *http.Request) {
	t, _ := template.ParseFiles("rdaf.html")
	t.Execute(w, nil)
}

func validatePage(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()
	if err != nil {
		w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
	}
	var url string
	for key, values := range r.Form {
		for _, value := range values {
			if key == "webURL" {
				url = value
			}
		}
	}

	var organization Organization

	// Need to validate the URL.
	doc, err := goquery.NewDocument(url)
	if err != nil {
		log.Fatal(err)
	}

	// This needs to be refactored to be cleaner, probably send to another
	// service that uses javascript then return to Go for additional validation.
	doc.Find("meta").Each(func(index int, item *goquery.Selection) {
		switch {
		case item.AttrOr("property", "") == "name":
			organization.Name = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "description":
			organization.Description = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "url":
			organization.URL = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "address":
			organization.Address = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "contactPoint":
			organization.ContactPoint = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "email":
			organization.Email = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "founder":
			organization.Founder = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "foudingDate":
			organization.FoundingDate = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "foudingLocation":
			organization.FoundingLocation = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "funder":
			organization.Funder = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "logo":
			organization.Logo = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "memberOf":
			organization.MemberOf = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "numberOfEmployees":
			organization.NumberOfEmployees = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "parentOrganization":
			organization.ParentOrganization = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "taxID":
			organization.TaxID = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "telephone":
			organization.Telephone = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "ethics":
			organization.Ethics = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "mission":
			organization.Mission = item.AttrOr("content", "")
		case item.AttrOr("property", "") == "diversity":
			organization.Diversity = item.AttrOr("content", "")
		}
	})

	tmpl, err := template.ParseFiles("validateTemplate.html")
	if err != nil {
		w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
	}
	tmpl.Execute(w, organization)
}

func generatePage(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()
	if err != nil {
		w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
	}

	var organization Organization
	loadObject(&organization, r.Form)
	fmt.Println(organization)
	tmpl, err := template.ParseFiles("generateTemplate.html")
	if err != nil {
		w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
	}
	tmpl.Execute(w, organization)
}

// Map the form to the organization structure. Needs to be beefed up.
func loadObject(obj interface{}, m map[string][]string) {
	val := reflect.ValueOf(obj).Elem()
	// Loop over form into nested form value.
	for k, values := range m {
		for _, v := range values {
			if f := val.FieldByName(k); f.IsValid() {
				if f.CanSet() {
					switch f.Type().Kind() {
					// Only need string for now.
					case reflect.String:
						f.SetString(v)
					case reflect.Array:
						f.SetString(v)
					default:
						fmt.Printf("Unsupported format %v for field %s\n", f.Type().Kind(), k)
					}
				} else {
					fmt.Printf("Key '%s' cannot be set\n", k)
				}
			} else {
				fmt.Printf("Key '%s' does not have a corresponding field in obj %+v\n", k, obj)
			}
		}
	}
}

func main() {
	http.HandleFunc("/", Docs)
	http.HandleFunc("/validate", Validate)
	http.HandleFunc("/generate", Generate)
	http.HandleFunc("/test", TestPage)

	defer func() {
		if err := recover(); err != nil {
		}
	}()

	err := http.ListenAndServe(":9494", nil)
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
	os.Exit(1)
}

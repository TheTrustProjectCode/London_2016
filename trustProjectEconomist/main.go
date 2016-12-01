package main

import (
	"fmt"
	"html/template"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"reflect"
	"time"

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
		generatePage(w, r)
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
	var webURL *http.Request
	for key, values := range r.Form {
		for _, value := range values {
			if key == "webURL" {
				webURL, err = http.NewRequest("GET", value, nil)
				if err != nil {
					w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
				}
			}
		}
	}

	resp, err := makeRequest(webURL)
	if err != nil {
		w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
	}

	doc, err := goquery.NewDocumentFromResponse(resp)
	if err != nil {
		w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
	}

	var organization Organization
	// This should likely be replaced with an actual form processing tool.
	doc.Find("meta").Each(func(index int, item *goquery.Selection) {
		val := reflect.ValueOf(&organization).Elem()
		key := item.AttrOr("property", "")
		value := item.AttrOr("content", "")
		if f := val.FieldByName(key); f.IsValid() {
			if f.CanSet() {
				switch f.Type().Kind() {
				// Only need string for now.
				case reflect.String:
					f.SetString(value)
				case reflect.Array:
					f.SetString(value)
				default:
					fmt.Printf("Unsupported format %v for field %s\n", f.Type().Kind(), key)
				}
			} else {
				fmt.Printf("Key '%s' cannot be set\n", key)
			}
		} else {
			fmt.Printf("Key '%s' does not have a corresponding field in obj %+v\n", key, organization)
		}
	})

	tmpl, err := template.ParseFiles("validateTemplate.html")
	if err != nil {
		w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
	}
	tmpl.Execute(w, &organization)
}

func makeRequest(req *http.Request) (*http.Response, error) {
	tr := &http.Transport{}

	client := &http.Client{
		Transport: tr,
		Timeout:   10 * time.Second,
	}

	resp, err := client.Do(req)

	return resp, err
}

func generatePage(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()
	if err != nil {
		w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
	}

	var organization Organization
	val := reflect.ValueOf(&organization).Elem()
	for k, values := range r.Form {
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
				fmt.Printf("Key '%s' does not have a corresponding field in obj %+v\n", k, organization)
			}
		}
	}

	tmpl, err := template.ParseFiles("generateTemplate.html")
	if err != nil {
		w.Write([]byte(`{"code":500,"message":"Internal Server Error"}`))
	}

	err = tmpl.Execute(w, &organization)
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

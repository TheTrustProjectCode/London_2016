package main

type Organization struct {
	Name               string `json:"name"`
	Description        string `json:"description"`
	URL                string `json:"url"`
	Address            string `json:"address"`
	ContactPoint       string `json:"contactPoint"`
	Email              string `json:"email"`
	Founder            string `json:"founder"`
	FoundingDate       string `json:"foundingDate"`
	FoundingLocation   string `json:"foundingLocation"`
	Funder             string `json:"funder"`
	LegalName          string `json:"legalName"`
	Logo               string `json:"logo"`
	MemberOf           string `json:"memberOf"`
	NumberOfEmployees  string `json:"numberofemployees"`
	ParentOrganization string `json:"parentOrganization"`
	TaxID              string `json:"taxID"`
	Telephone          string `json:"telephone"`
	Ethics             string `json:"ethics"`
	Diversity          string `json:"diversity"`
	Mission            string `json:"mission"`
}

type Address struct {
	AddressCountry      string
	AddressLocality     string
	AddressRegion       string
	PostOfficeBoxNumber string
	PostalCode          string
	StreetAddress       string
}

type Person struct {
	Name          string
	AlternateName string
	Description   string
	Addresss      Address
	WorksFor      string
	Telephone     string
}

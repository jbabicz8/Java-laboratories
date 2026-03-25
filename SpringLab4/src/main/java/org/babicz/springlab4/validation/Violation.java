package org.babicz.springlab4.validation;

public record Violation(String fieldName, String errorMessage) {
}

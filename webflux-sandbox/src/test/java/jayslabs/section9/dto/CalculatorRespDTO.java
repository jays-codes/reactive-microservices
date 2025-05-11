package jayslabs.section9.dto;

public record CalculatorRespDTO(
    Integer first, 
    Integer second, 
    String operation, 
    Double result) {
}

package com.umograd.analytic.util;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExpressionEvaluator {

    private final ExpressionParser parser = new SpelExpressionParser();

    public boolean evaluateBoolean(String expressionString, Map<String, Object> contextVariables) {
        try {
            StandardEvaluationContext context = new StandardEvaluationContext();

            if (contextVariables != null) {
                contextVariables.forEach(context::setVariable);
            }

            Expression expression = parser.parseExpression(expressionString);
            Boolean result = expression.getValue(context, Boolean.class);

            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            System.err.println("Ошибка при вычислении SpEL-выражения [" + expressionString + "]: " + e.getMessage());
            return false;
        }
    }
}

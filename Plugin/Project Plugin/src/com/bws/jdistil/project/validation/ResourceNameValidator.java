package com.bws.jdistil.project.validation;

import java.util.Arrays;
import java.util.List;

/**
 * Verifies resource names.
 */
public class ResourceNameValidator {

  /**
   * List of Java keywords.
   */
  private static final List<String> javaKeywords = Arrays.asList("abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long	native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false", "null");

  /**
   * List of SQL keywords.
   */
  private static final List<String> sqlKeywords = Arrays.asList("ABORT","ACCELERATED","ADD","AFTER","ALL","ALTER","AND","ANSI-PADDING","ANY","AS","ASC","ATOMIC","AVG","BEFORE","BEGIN","BETWEEN","BORDER","BY","CALL","CACHED_PROCEDURES","CASCADE","CASE","CAST","CHECK","CLOSE","COALESCE","COLLATE","COLUMN","COMMIT","COMMITTED","CONSTRAINT","CONVERT","COUNT","CREATE","CREATESP","CREATETAB","CREATEVIEW","CROSS","CS","CURDATE","CURRENT","CURSOR","CURTIME","DATA_PATH","DATABASE","DATETIMEMILLISECONDS","DBO","DBSEC_AUTHENTICATION","DBSEC_AUTHORIZATION","DCOMPRESS","DDF","DECIMALSEPARATORCOMMA","DECLARE","DEFAULT","DEFAULTCOLLATE","DELETE","DENY","DESC","DIAGNOSTICS","DICTIONARY","DICTIONARY_PATH","DISTINCT","DO","DROP","DSN","EACH","ELSE","ENCODING","END","ENFORCED","EX","EXCLUSIVE","EXEC","EXECUTE","EXISTING","EXISTS","EXPR","FETCH","FILES","FN","FOR","FOREIGN","FROM","FULL","FUNCTION","GLOBAL_QRYPLAN","GRANT","GROUP","HANDLER","HAVING","IDENTITY","IF","IN","INDEX","INNER","INOUT","INSERT","INTEGRITY","INTERNAL","INTO","IS","ISOLATION","JOIN","KEY","LEAVE","LEFT","LEGACYOWNERNAME","LEVEL","LIKE","LIMIT","LINKDUP","LOGIN","LOOP","MAX","MIN","MODE","MODIFIABLE","MODIFY","NEW","NEXT","NO","NO_REFERENTIAL_INTEGRITY","NORMAL","NOT","NOW","NULL","OF","OFF","OFFSET","OLD","ON","ONLY","OPEN","OPTINNERJOIN","OR","ORDER","OUT","OUTER","OWNER","PAGESIZE","PARTIAL","PASSWORD","PCOMPRESS","PRED","PRIMARY","PRINT","PROCEDURE","PROCEDURES_CACHE","PSQL_MOVE","PSQL_PHYSICAL","PSQL_POSITION","PUBLIC","QRYPLAN","QRYPLANOUTPUT","READ","REFERENCES","REFERENCING","RELATIONAL","RELEASE","RENAME","REPEAT","REPEATABLE","REPLACE","RESTRICT","RETURN","RETURNS","REUSE_DDF","REVERSE","REVOKE","RIGHT","ROLLBACK","ROW","ROWCOUNT","ROWCOUNT2","SAVEPOINT","SECURITY","SELECT","SERIALIZABLE","SESSIONID","SET","SIGNAL","SIZE","SPID","SQLSTATE","SSP_EXPR","SSP_PRED","START","STDEV","SUM","SVBEGIN","SVEND","TABLE","THEN","TO","TOP","TRANSACTION","TRIGGER","TRIGGERSTAMPMISC","TRUEBITCREATE","TRUENULLCREATE","TRY_CAST","TS","UNCOMMITTED","UNION","UNIQUE","UNIQUEIDENTIFIER","UNTIL","UPDATE","USER","USING","V1_METADATA","V2_METADATA","VALUES","VIEW","WHEN","WHERE","WHILE","WITH","WORK","WRITE");

  /**
   * Error message.
   */
  private String errorMessage = null;
  
  /**
   * Creates a new resource name validator.
   */
  public ResourceNameValidator() {
    super();
  }
  
  /**
   * Returns a value indicating whether or not a specified resource name is valid.
   * @param fieldName Field name.
   * @param resourceName Resource name.
   */
  public boolean isValid(String fieldName, String resourceName) {

    // Initialize return value
    boolean isValid = true;

    // Clear error message
    errorMessage = null;
    
    if (resourceName == null || resourceName.trim().length() == 0) {

      // Set valid indicator and error message
      isValid = false;
      errorMessage = fieldName + " is required.";
    }
    else if (javaKeywords.contains(resourceName.toLowerCase())) {

      // Set valid indicator and error message
      isValid = false;
      errorMessage = fieldName + " cannot be a Java keyword.";
    }
    else if (sqlKeywords.contains(resourceName.toUpperCase())) {

      // Set valid indicator and error message
      isValid = false;
      errorMessage = fieldName + " cannot be a SQL keyword.";
    }
    
    return isValid;
  }

  /**
   * Returns the error message associated with the last validation. 
   * @return String Error message.
   */
  public String getErrorMessage() {
    return errorMessage;
  }
  
}

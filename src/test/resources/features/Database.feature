Feature: DATABASE steps usage examples

  Usage:
    gradlew testspec -Dfeatures=features/Database.feature

  Scenario: first
    Given imported files features/Database.groovy

    # IF NOT EXISTS
    When I execute statements on database h2:
    """
    CREATE TABLE ONE (
        ID int NOT NULL,
        NAME varchar(32) NOT NULL
    );
    """

    When I execute statements on database h2:
    """
    INSERT INTO ONE VALUES (1, 'one');
    INSERT INTO ONE VALUES (2, 'two');
    """

    When I capture into my_dataset result of query on database h2:
    """
    SELECT * FROM ONE;
    """

    Then I execute code "to verify my dataset":
    """
    assert my_dataset == [
      [ID: 1, NAME: 'one'],
      [ID: 2, NAME: 'two']
    ]

    assert my_dataset[0]['ID'] == 1
    """

  # TODO: implement
#    Then named query 'my' should return:
#      | ID | NAME |
#      | 1  | one  |
#      | 2  | two  |


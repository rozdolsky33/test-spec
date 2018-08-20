package org.edushak.testspec.util

import org.edushak.testspec.BaseSpec

class HelperSpec extends BaseSpec {

    def "readCsv"() {
        given:
        File csvFile = new File("$TEST_RESOURCES_DIR/unit/test.csv")

        when:
        List<List> dataRows = Helper.readCsvAsList(csvFile)

        then:
        dataRows == [
            ['1', 'Ed', 'New York'],
            ['2', 'Leo', 'New Jersey']
        ]
    }

    def "resolveFile"() {
        when:
        File resolvedFile = Helper.resolveFile('unit/test.csv', false)

        then:
        resolvedFile != null && resolvedFile.exists()
    }
}
